package ort.proyecto.gestac.core.agents;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.score.KnowledgeScoreHelper;

public class KnowledgeScoreAgent extends GestacAgent {
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeScoreAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
	private int tickerInterval;
	
	private String mode;

	
	public KnowledgeScoreAgent(String mode) {
		super();
		this.mode = mode;
	}
	
	@Override
	protected void setup() {
		if (mode.equals("ticker")) {
			addBehaviour(new SearchKnowledgesToUpdateBehaviour(this, tickerInterval));
			addBehaviour(new SearchBestKnowledgeBehaviour(this, tickerInterval));
		} else if (mode.equals("update")){
			addBehaviour(new UpdateKnowledgeBehaviour());
		}
	}
	
	class UpdateKnowledgeBehaviour extends OneShotBehaviour {
		/**
		 * para todo este proceso no espero respuestas, solo mando cosas y asumo que se hacen.
		 */
		
		@Override
		public void action() {
			ACLMessage request = blockingReceive(MessageTemplate.MatchReplyWith("updatedScore"));
			try {
				Knowledge knowledge = getJsonMapper().readValue(request.getContent(), Knowledge.class);
				knowledge.setKnowledgeScore(KnowledgeScoreHelper.calculateScore(knowledge));
				knowledge.setConsideredEvaluations(knowledge.getEvaluations().size());
				
				//guardar knowledge actualizado.
				ACLMessage updateKnowledge = createMessage("KnowledgeDBAgent");
				updateKnowledge.setContent(DBAgentOperations.UPDATE_KNOWLEDGE+
						"&"+getJsonMapper().writeValueAsString(knowledge));
				send(updateKnowledge);
				
				//enviar mensaje a fuente para actualizarse.
				ACLMessage updateSource = createMessage("SourceScoreAgent");
				updateSource.setContent("updateSourceOnEvaluation"+"&"+
						getJsonMapper().writeValueAsString(knowledge));
				send(updateSource);
				
			} catch (Exception e) {
				logger.error("Error updating knowledge, " + request.getContent(), e);
			} finally {
				this.myAgent.doDelete();				
			}
		}
		
	}

	class SearchKnowledgesToUpdateBehaviour extends TickerBehaviour {
		
		public SearchKnowledgesToUpdateBehaviour(Agent agent, long interval) {
			super(agent, interval);
		}
		
		@Override
		protected void onTick() {			
			//System.out.println("################### tick, " + new Timestamp(System.currentTimeMillis()));
			try {
				ACLMessage message = createMessage("KnowledgeDBAgent");
				message.setContent(DBAgentOperations.SEARCH_KNOWLEDGES_TO_UPDATE);
				send(message);
				ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
				if (reply.getContent()!=null) {
					List<Knowledge> toUpdate = Arrays.asList(getJsonMapper().readValue(reply.getContent(), Knowledge[].class));
					for (Knowledge knowledge : toUpdate) {
						
						KnowledgeScoreAgent scoreAgent = new KnowledgeScoreAgent("update");
						this.myAgent.getContainerController().acceptNewAgent("KnowledgeScoreAgent"+knowledge.getId(), 
								scoreAgent).start();
						ACLMessage scoreMessage = createMessage("KnowledgeScoreAgent"+knowledge.getId());
						scoreMessage.setContent(getJsonMapper().writeValueAsString(knowledge));
						scoreMessage.setReplyWith("updatedScore");
						send(scoreMessage);
					}
				}
			} catch (IOException e) {
				logger.error("Error parsing on ticker", e);
			} catch (StaleProxyException e) {
				logger.error("Error on ticker", e);
			}				
		}
	}
		
	class SearchBestKnowledgeBehaviour extends TickerBehaviour {
		
		public SearchBestKnowledgeBehaviour(Agent agent, long interval) {
			super(agent, interval);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			try {
				Thread.sleep(tickerInterval/2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected void onTick() {			
			//System.out.println("################### tick best, " + new Timestamp(System.currentTimeMillis()));
			
			ACLMessage message = createMessage("KnowledgeDBAgent");
			message.setContent(DBAgentOperations.SEARCH_AND_UPDATE_BEST_KNOWLEDGES_FOR_ISSUES);
			send(message);				
		}
		
	}


	public int getTickerInterval() {
		return tickerInterval;
	}


	public void setTickerInterval(int tickerInterval) {
		this.tickerInterval = tickerInterval;
	}

	
}
