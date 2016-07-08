package ort.proyecto.gestac.core.agents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import ort.proyecto.gestac.core.entities.Issue;

public class IssueSearchAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private Map<String, List<Issue>> results = new HashMap<>();
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new SearchBehaviour());
	}
	
	
	class SearchBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
		
			try {
				ACLMessage message = blockingReceive();
				String content = message.getContent();
				String[] parameters = content.split("&");
				String operation = parameters[0];
				switch (operation){
				case "getIssues":
					IssueAgent issueAgentSubjectIncidentGravity = new IssueAgent(parameters[1], parameters[2], parameters[3], parameters[4]);
					AgentController controller = this.myAgent.getContainerController().acceptNewAgent("agenteprueba", issueAgentSubjectIncidentGravity);
					controller.start();
					System.out.println("### - IssueSearchAgent, contenedor: " + this.myAgent.getContainerController().getContainerName());
					ACLMessage messageToPrueba = crearMensaje("agenteprueba");
					messageToPrueba.setContent("");
			        send(messageToPrueba);
			        ACLMessage replyFromPrueba = blockingReceive();
			        System.out.println("### - IssueSearchAgent, finalizado");
				}
				
			} catch (Exception e) {
				System.out.println(e);
			}			
		}
		
	}
	
	private ACLMessage crearMensaje(String agentName) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        AID idAgente = new AID(agentName, AID.ISLOCALNAME);
        message.addReceiver(idAgente);
        message.setConversationId(UUID.randomUUID().toString());
        return message;
    }

}
