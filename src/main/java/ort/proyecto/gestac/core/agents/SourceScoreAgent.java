package ort.proyecto.gestac.core.agents;

import java.io.IOException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.Source;
import ort.proyecto.gestac.core.entities.score.SourceScoreHelper;

public class SourceScoreAgent extends GestacAgent {
	
	private Logger logger = LoggerFactory.getLogger(SourceScoreAgent.class);

	private String mode;
	
	private int tickerInterval = 5000;

	public SourceScoreAgent(String mode) {
		super();
		this.mode = mode;
	}
	
	@Override
	protected void setup() {
		if (mode.equals("update")) {
			addBehaviour(new UpdateBehaviour());		
		} else if (mode.equals("evaluate")) {
			addBehaviour(new DoEvaluationBehaviour());
		} else if (mode.equals("ticker")) {
			addBehaviour(new SearchBestSourceBehaviour(this, tickerInterval));
		}
	}
	
	class SearchBestSourceBehaviour extends TickerBehaviour {

		public SearchBestSourceBehaviour(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			ACLMessage message = createMessage("SourceDBAgent");
			message.setContent(DBAgentOperations.SEARCH_AND_UPDATE_BEST_SOURCE_FOR_AREA);
			send(message);
		}
		
	}

	class UpdateBehaviour extends CyclicBehaviour {
		
		private Logger logger = LoggerFactory.getLogger(UpdateBehaviour.class);

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				String[] parameters = message.getContent().split("&");
				String operation = parameters[0];
				switch (operation){
				case "updateSourceOnEvaluation":
					try {
						Knowledge knowledge = getJsonMapper().readValue(parameters[1], Knowledge.class); 
						
						SourceScoreAgent scoreAgent = new SourceScoreAgent("evaluate");
						this.myAgent.getContainerController().acceptNewAgent("SourceScoreAgent"+knowledge.getId(), 
								scoreAgent).start();
						ACLMessage scoreMessage = createMessage("SourceScoreAgent"+knowledge.getId());
						scoreMessage.setContent(parameters[1]);
						scoreMessage.setReplyWith("updatedScore");
						send(scoreMessage);
					} catch (StaleProxyException e) {
						logger.error("Error adding agent to container",e);
					} catch (IOException e) {
						logger.error("Error parsing knowledge parameter, " + parameters[1], e);
					}
					
//					KnowledgeScoreAgent scoreAgent = new KnowledgeScoreAgent("update");
//					this.myAgent.getContainerController().acceptNewAgent("KnowledgeScoreAgent"+knowledge.getId(), 
//							scoreAgent).start();
//					ACLMessage scoreMessage = createMessage("KnowledgeScoreAgent"+knowledge.getId());
//					scoreMessage.setContent(getJsonMapper().writeValueAsString(knowledge));
//					scoreMessage.setReplyWith("updatedScore");
//					send(scoreMessage);
					
					break;
				}
			} else {
				block();
			}
		}
		
	}
	
	class DoEvaluationBehaviour extends OneShotBehaviour {

		private Logger logger = LoggerFactory.getLogger(DoEvaluationBehaviour.class);
		
		@Override
		public void action() {
			ACLMessage request = null;
			try {
				request = blockingReceive(MessageTemplate.MatchReplyWith("updatedScore"));
				Knowledge knowledge = getJsonMapper().readValue(request.getContent(), Knowledge.class);
				
				Source source = knowledge.getSource();
				source.setScoreTotal(SourceScoreHelper.calculateTotalEvaluation(source, knowledge.getEvaluations()));
				source.setEvaluationUpdated(new Timestamp(System.currentTimeMillis()));
				
				ACLMessage updateSource = createMessage("SourceDBAgent");
				updateSource.setContent(DBAgentOperations.UPDATE_SOURCE_EVALUATION+
						"&"+source.getId()+"&"+source.getScoreTotal()+"&"+source.getEvaluationUpdated().getTime());
				send(updateSource);
			} catch (IOException e) {
				logger.error("Error parsing knowledge, " + request.getContent()!=null?request.getContent():"request content is empty", e);
			} finally {
				this.myAgent.doDelete();
			}
		}
		
	}

	public int getTickerInterval() {
		return tickerInterval;
	}

	public void setTickerInterval(int tickerInterval) {
		this.tickerInterval = tickerInterval;
	}
	
}
