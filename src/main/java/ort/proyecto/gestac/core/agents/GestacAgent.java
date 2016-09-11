package ort.proyecto.gestac.core.agents;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

public class GestacAgent extends Agent {

	private ObjectMapper jsonMapper = new ObjectMapper();
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
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
	
	protected void sendEmptyReply(ACLMessage messageToReplyTo) {
		ACLMessage reply = messageToReplyTo.createReply();
		send(reply);
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

	@Override
	protected void setup() {
		super.setup();
		try {
			agentsLogger.info(this.getName() + " started, container: " + this.getContainerController().getContainerName());
		} catch (ControllerException e) {
		}
	}
	
}
