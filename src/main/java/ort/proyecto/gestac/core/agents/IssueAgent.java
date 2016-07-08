package ort.proyecto.gestac.core.agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

public class IssueAgent extends Agent {

	private String areaId;
	private String subjectId;
	private String incidentId;
	private String gravityId;
	
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new myBehaviour());
	}
	
	/**
	 * según los datos que haya, llamar a dbagent
	 * @return
	 */
	class myBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			try {
				ACLMessage message = blockingReceive();
				System.out.println("### - IssueAgent, contenedor: " + this.myAgent.getContainerController().getContainerName());
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}
	public String getGravityId() {
		return gravityId;
	}
	public void setGravityId(String gravityId) {
		this.gravityId = gravityId;
	}
	public IssueAgent(String areaId, String subjectId, String incidentId, String gravityId) {
		super();
		this.areaId = areaId;
		this.subjectId = subjectId;
		this.incidentId = incidentId;
		this.gravityId = gravityId;
	}
	
	
	
}
