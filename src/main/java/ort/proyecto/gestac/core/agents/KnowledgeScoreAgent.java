package ort.proyecto.gestac.core.agents;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.sun.jmx.snmp.Timestamp;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;
import ort.proyecto.gestac.core.entities.score.KnowledgeScoreHelper;

public class KnowledgeScoreAgent extends GestacAgent {
	
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
			try {
				ACLMessage request = blockingReceive(MessageTemplate.MatchReplyWith("updatedScore"));
				Knowledge knowledge = getJsonMapper().readValue(request.getContent(), Knowledge.class);
				knowledge.setKnowledgeScore(KnowledgeScoreHelper.calculateScore(knowledge));
				knowledge.setConsideredEvaluations(knowledge.getEvaluations().size());
				
				//guardar knowledge actualizado.
				
				//enviar mensaje a fuente para actualizarse.
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				this.myAgent.doDelete();				
			}
		}
		
	}

	class SearchKnowledgesToUpdateBehaviour extends TickerBehaviour {
		
		private boolean updateOnCourse = false;
		
		public SearchKnowledgesToUpdateBehaviour(Agent agent, long interval) {
			super(agent, interval);
		}
		
		@Override
		protected void onTick() {			
			if (!updateOnCourse) {
				System.out.println("################### tick, " + new Timestamp(System.currentTimeMillis()));
				try {
					ACLMessage message = createMessage("KnowledgeDBAgent");
					message.setContent(DBAgentOperations.SEARCH_KNOWLEDGES_TO_UPDATE);
					send(message);
					ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
					if (reply.getContent()!=null) {
						updateOnCourse = true;
						List<Knowledge> toUpdate = Arrays.asList(getJsonMapper().readValue(reply.getContent(), Knowledge[].class));
						for (Knowledge knowledge : toUpdate) {
							//resuelvo acá o instancio nuevos agentes?
							//la gracia de instanciar agentes es paralelismo...
							
							KnowledgeScoreAgent scoreAgent = new KnowledgeScoreAgent("update");
							this.myAgent.getContainerController().acceptNewAgent("KnowledgeScoreAgent"+knowledge.getId(), 
									scoreAgent).start();
							ACLMessage scoreMessage = createMessage("KnowledgeScoreAgent"+knowledge.getId());
							scoreMessage.setContent(getJsonMapper().writeValueAsString(knowledge));
							scoreMessage.setReplyWith("updatedScore");
							send(scoreMessage);
//							//agregar el primer agente
//							IssueAgent issueAgentSubjectIncidentGravity = new IssueAgent(parameters[1], parameters[2], parameters[3], parameters[4], issueSearch);
//							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&1", 
//									issueAgentSubjectIncidentGravity).start();
//							//enviar mensaje a primer agente
//							ACLMessage messageSearchSubjectIncidentGravity = createMessage("IssueAgent&"+conversationId+"&1", conversationId);
//							send(messageSearchSubjectIncidentGravity);
						}
						if (toUpdate!=null && toUpdate.size()>0) {
							/*
							 * actualizar mejor conocimiento para issue.
							 */
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			}
//			ACLMessage reply = receive(pendingResponse);
//			if (reply!=null) {
//				System.out.println("reply " + reply);
//				pendingResponse = null;
//			} else {
//				if (pendingResponse==null) {
//					ACLMessage message = createMessage("KnowledgeDBAgent");
//					message.setContent(DBAgentOperations.SEARCH_KNOWLEDGES_TO_UPDATE);
//					pendingResponse = MessageTemplate.MatchConversationId(message.getConversationId());
//					send(message);
//					block();
//				}
//			}				
		}
		
	}


	public int getTickerInterval() {
		return tickerInterval;
	}


	public void setTickerInterval(int tickerInterval) {
		this.tickerInterval = tickerInterval;
	}

	
}
