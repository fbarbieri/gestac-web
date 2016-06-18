package ort.proyecto.gestac.core.agents;

import java.util.List;
import java.util.UUID;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Subject;

public class InterfaceAgent extends GuiAgent {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public List<Area> getAreas() {
		List<Area> list = null;
		try {
			ACLMessage message = crearMensaje("DBAgent");
			message.setContent(String.valueOf(DBAgentOperations.FIND_ALL_AREAS));
	        send(message);
	        ACLMessage reply = blockingReceive();
			List<Area> areas = (List<Area>) reply.getContentObject();
			Thread.currentThread().getContextClassLoader().loadClass("ort.proyecto.gestac.core.entities.Area");
			Area area = areas.get(0);
			for (Subject s : area.getSubjects()) {
				System.out.println(s);
			}
	        System.out.println(areas.get(0).getSubjects().iterator().next().getName());
	        System.out.println(areas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private ACLMessage crearMensaje(String agentName) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        AID idAgente = new AID(agentName, AID.ISLOCALNAME);
        message.addReceiver(idAgente);
        message.setConversationId(UUID.randomUUID().toString());
        return message;
    }
	
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		System.out.println(ev.getType());
		System.out.println(ev.getSource());
	}

}
