package ort.proyecto.gestac.core.agents;

import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class GestacAgent extends Agent {

	protected ACLMessage createMessage(String agentName) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        AID idAgente = new AID(agentName, AID.ISLOCALNAME);
        message.addReceiver(idAgente);
        message.setConversationId(UUID.randomUUID().toString());
        return message;
    }
	
	protected ACLMessage createMessage(String agentName, String conversationId) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        AID idAgente = new AID(agentName, AID.ISLOCALNAME);
        message.addReceiver(idAgente);
        message.setConversationId(conversationId);
        return message;
    }
	
}
