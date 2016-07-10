package ort.proyecto.gestac.core.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.repository.IssueRepository;
import ort.proyecto.gestac.core.entities.repository.IssueSearchRepository;

public class IssueSearchAgent extends GestacAgent {
	
	private static final long serialVersionUID = 1L;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private Map<String, List<Issue>> firstResults = new HashMap<>();
	
	private Map<String, List<Issue>> secondResults = new HashMap<>();
	
	private Map<String, List<Issue>> thirdResults = new HashMap<>();
	
	private Map<String, Integer> replies = new HashMap<>();
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private IssueSearchRepository issueSearch;
	
	
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
					if (sender.equals("InterfaceAgent")) {
						String[] parameters = content.split("&");
						String operation = parameters[0];
						switch (operation){
						case "getIssues":
							//agregar la conversación a resultados
							firstResults.put(conversationId, new ArrayList<>());
							secondResults.put(conversationId, new ArrayList<>());
							thirdResults.put(conversationId, new ArrayList<>());
							//agregar la conversación a control de respuestas
							replies.put(conversationId, 0);
							
							//agregar el primer agente
							IssueAgent issueAgentSubjectIncidentGravity = new IssueAgent(parameters[1], parameters[2], parameters[3], parameters[4], issueSearch);
							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&1", 
									issueAgentSubjectIncidentGravity).start();
							//enviar mensaje a primer agente
							ACLMessage messageSearchSubjectIncidentGravity = createMessage("IssueAgent&"+conversationId+"&1", conversationId);
							send(messageSearchSubjectIncidentGravity);

							//agregar el segundo agente
							IssueAgent issueAgentSubjectIncident= new IssueAgent(parameters[1], parameters[2], parameters[3], null, issueSearch);
							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&2", 
									issueAgentSubjectIncident).start();
							//enviar mensaje a segundo agente
							ACLMessage messageSearchSubjectIncident = createMessage("IssueAgent&"+conversationId+"&2", conversationId);
							send(messageSearchSubjectIncident);
							
							//agregar el tercer agente
							IssueAgent issueAgentSubjectGravity= new IssueAgent(parameters[1], parameters[2], null, parameters[4], issueSearch);
							this.myAgent.getContainerController().acceptNewAgent("IssueAgent&"+conversationId+"&3", 
									issueAgentSubjectGravity).start();
							//enviar mensaje a tercer agente
							ACLMessage messageSearchSubjectGravity = createMessage("IssueAgent&"+conversationId+"&3", conversationId);
							send(messageSearchSubjectGravity);
							
						}
						
					} 
					if (sender.startsWith("IssueAgent")) {
						System.out.println("llegada de respuesta " + message.getConversationId());
						if (replies.get(conversationId)!=null){
							//llegó una respuesta válida
							
							/**
							 * ordenar?
							 */
							
							//sumo la cantidad de respuestas
							int quantity = replies.get(conversationId);
							replies.remove(conversationId);
							replies.put(conversationId, quantity+1);
							
							//agrego los resultados
							List<Issue> contentResult = new ArrayList<>();//Arrays.asList(jsonMapper.readValue(content, Issue[].class));
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
							
							//comprobar si se recibió todo
							if (replies.get(conversationId).equals(3)) {
								System.out.println("llegaron todas las respuestas, "  + message.getConversationId());
								List<Issue> finalResults = mergeResults(conversationId);
								//enviar respuestas
								ACLMessage replyToInterface = createMessage("InterfaceAgent", conversationId);
								replyToInterface.setContent(jsonMapper.writeValueAsString(finalResults.toArray()));
								send(replyToInterface);
							}
						}
					}
					
					
					/**
					 * cuando arranco a mandar los mensajes, pongo el conversationid en el mapa.
					 * si el mensaje es de respuesta, lo agrego al map de listas y de cantidades (otro mapa que vaya llevando la cuenta de cuantas respuestas recibo)
					 * si la respuesta es la última, uno los resultados y contesto el mensaje.
					 * 
					 * mantener un mapa con los mensajes originales desde la interfaz?
					 * 
					 */					
				} else {
					block();
				}
				
			} catch (Exception e) {
				System.out.println(e);
				//myAgent.doDelete();
			}			
		}
		
	}
	
	protected List<Issue> mergeResults(String conversationId) {
		List<Issue> merged = new ArrayList<>();
		List<String> addedIds = new ArrayList<>();
		
		HashMap<String, List<Issue>> results = new HashMap<>();
		results.put(conversationId, firstResults.get(conversationId));
		results.get(conversationId).addAll(secondResults.get(conversationId));
		results.get(conversationId).addAll(thirdResults.get(conversationId));
		
		for (Issue i : results.get(conversationId)) {
			if (!addedIds.contains(i.getId())) {
				merged.add(i);
			}
		}
		return merged;
	}
	
	

}
