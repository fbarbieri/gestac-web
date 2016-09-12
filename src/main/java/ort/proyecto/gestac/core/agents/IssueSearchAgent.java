package ort.proyecto.gestac.core.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.repository.IssueRepository;
import ort.proyecto.gestac.core.entities.repository.IssueSearchDataSource;

public class IssueSearchAgent extends GestacAgent {
	
	private Logger logger = LoggerFactory.getLogger(IssueSearchAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
	private static final long serialVersionUID = 1L;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private Map<String, List<Issue>> firstResults = new HashMap<>();
	
	private Map<String, List<Issue>> secondResults = new HashMap<>();
	
	private Map<String, List<Issue>> thirdResults = new HashMap<>();
	
	private Map<String, Integer> replies = new HashMap<>();
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private IssueSearchDataSource issueSearch;
	
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new SearchBehaviour());
	}
	
	
	class SearchBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
		
			try {
				ACLMessage message = receive();
				if (message!=null) {
					String content = message.getContent();
					String sender = message.getSender().getLocalName();
					String conversationId = message.getConversationId();
					agentsLogger.debug(this.myAgent.getName() + ", message from " + message.getSender() +" recieved: " + content + ", conversationId:" + message.getConversationId());
					/**
					 * 
					 */
					if (sender.equals("InterfaceAgent")) {
						String[] parameters = content.split("&");
						String operation = parameters[0];
						switch (operation){
						case "getIssues":
							//agregar la conversaci�n a resultados
							firstResults.put(conversationId, new ArrayList<>());
							secondResults.put(conversationId, new ArrayList<>());
							thirdResults.put(conversationId, new ArrayList<>());
							//agregar la conversaci�n a control de respuestas
							replies.put(conversationId, 0);
							
							//agregar el primer agente
							IssueAgent issueAgentSubjectIncidentGravity = new IssueAgent(parameters[1], parameters[2], parameters[3], parameters[4], issueSearch);
							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&1", 
									issueAgentSubjectIncidentGravity).start();
							//enviar mensaje a primer agente
							ACLMessage messageSearchSubjectIncidentGravity = createMessage("IssueAgent&"+conversationId+"&1", conversationId);
							agentsLogger.debug(this.myAgent.getName() + ", message to first IssueAgent: " + content + ", conversationId:" + conversationId);
							send(messageSearchSubjectIncidentGravity);

							//agregar el segundo agente
							IssueAgent issueAgentSubjectIncident= new IssueAgent(parameters[1], parameters[2], parameters[3], null, issueSearch);
							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&2", 
									issueAgentSubjectIncident).start();
							//enviar mensaje a segundo agente
							ACLMessage messageSearchSubjectIncident = createMessage("IssueAgent&"+conversationId+"&2", conversationId);
							agentsLogger.debug(this.myAgent.getName() + ", message to second IssueAgent: " + content + ", conversationId:" + conversationId);
							send(messageSearchSubjectIncident);
							
							//agregar el tercer agente
							IssueAgent issueAgentSubjectGravity= new IssueAgent(parameters[1], parameters[2], null, parameters[4], issueSearch);
							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&3", 
									issueAgentSubjectGravity).start();
							//enviar mensaje a tercer agente
							ACLMessage messageSearchSubjectGravity = createMessage("IssueAgent&"+conversationId+"&3", conversationId);
							agentsLogger.debug(this.myAgent.getName() + ", message to third IssueAgent: " + content + ", conversationId:" + conversationId);
							send(messageSearchSubjectGravity);
							
							break;
						}
						
					} 
					if (sender.startsWith("IssueAgent")) {
						agentsLogger.debug(this.myAgent.getName() + ", reply from a IssueAgent: " + content + ", conversationId:" + conversationId);
						if (replies.get(conversationId)!=null){
							//lleg� una respuesta v�lida
							
							//sumo la cantidad de respuestas
							int quantity = replies.get(conversationId);
							replies.remove(conversationId);
							replies.put(conversationId, quantity+1);
							
							//agrego los resultados
							if (content!=null) {
								List<Issue> contentResult = Arrays.asList(jsonMapper.readValue(content, Issue[].class));
								switch(message.getSender().getLocalName().substring(message.getSender().getLocalName().length()-1)){
								case "1":
									firstResults.get(conversationId).addAll(contentResult);
									break;
								case "2":
									secondResults.get(conversationId).addAll(contentResult);
									break;
								case "3":
									thirdResults.get(conversationId).addAll(contentResult);
									break;
								}
							}
							
							//comprobar si se recibi� todo
							if (replies.get(conversationId).equals(3)) {
								agentsLogger.debug(this.myAgent.getName() + ", all three replies from the IssueAgents arrived, merging results");
								List<Issue> finalResults = mergeResults(conversationId);
								//limpiar resultados
								replies.remove(conversationId);
								firstResults.remove(conversationId);
								secondResults.remove(conversationId);
								thirdResults.remove(conversationId);
								//enviar respuestas
								ACLMessage replyToInterface = createMessage("InterfaceAgent", conversationId);
								replyToInterface.setContent(jsonMapper.writeValueAsString(finalResults.toArray()));
								agentsLogger.debug(this.myAgent.getName() + ", send reply to InterfaceAgent: " + replyToInterface.getContent() + ", conversationId:" + replyToInterface.getConversationId());
								send(replyToInterface);
							}
						}
					}
					
					
					/**
					 * cuando arranco a mandar los mensajes, pongo el conversationid en el mapa.
					 * si el mensaje es de respuesta, lo agrego al map de listas y de cantidades (otro mapa que vaya llevando la cuenta de cuantas respuestas recibo)
					 * si la respuesta es la �ltima, unifico los resultados y contesto el mensaje.
					 */					
				} else {
					block();
				}
				
			} catch (Exception e) {
				logger.error("Error during search", e);
			}			
		}
		
	}
	
	protected List<Issue> mergeResults(String conversationId) {
		List<Issue> merged = new ArrayList<>();
		List<Long> addedIds = new ArrayList<>();
		
		HashMap<String, List<Issue>> results = new HashMap<>();
		if (firstResults.get(conversationId).size()>0) {
			results.put(conversationId, firstResults.get(conversationId));			
		}
		if (secondResults.get(conversationId).size()>0) {
			if (results.get(conversationId)!=null) {
				results.get(conversationId).addAll(secondResults.get(conversationId));							
			} else {
				results.put(conversationId, secondResults.get(conversationId));
			}
		}
		if (thirdResults.get(conversationId).size()>0) {
			if (results.get(conversationId)!=null) {
				results.get(conversationId).addAll(thirdResults.get(conversationId));							
			} else {
				results.put(conversationId, thirdResults.get(conversationId));
			}
		}
		for (Issue i : results.get(conversationId)) {
			if (!addedIds.contains(i.getId())) {
				merged.add(i);
				addedIds.add(i.getId());
			}
		}
		return merged;
	}
	
	

}
