package ort.proyecto.gestac.core.agents;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class GestacAgent extends Agent {

	private ObjectMapper jsonMapper = new ObjectMapper();
	
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
	
	protected void sendReply(List data, ACLMessage messageToReplyTo) {
		ACLMessage reply = messageToReplyTo.createReply();
		try {
			reply.setContent(jsonMapper.writeValueAsString(data.toArray()));
			send(reply);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	protected void sendReply(Object data, ACLMessage messageToReplyTo) {
		ACLMessage reply = messageToReplyTo.createReply();
		try {
			if (data!=null) {
				reply.setContent(jsonMapper.writeValueAsString(data));				
			}
			send(reply);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	protected void sendReply(String data, ACLMessage messageToReplyTo) {
		ACLMessage reply = messageToReplyTo.createReply();
		try {
			reply.setContent(data);
			send(reply);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}

	public ObjectMapper getJsonMapper() {
		return jsonMapper;
	}
	
}
