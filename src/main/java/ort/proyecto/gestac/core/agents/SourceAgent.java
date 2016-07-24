package ort.proyecto.gestac.core.agents;

public class SourceAgent extends GestacAgent {
	/**
	 * los que se agreguen por spring deben tener task=refresh
	 */

	public final String REFRESH = "refresh";
	public final String UPDATE = "update";
	
	private String task;
	
	@Override
	protected void setup() {	
		if (task.equals(REFRESH)) {
			addBehaviour(new SourceRefreshBehaviour());
		} else if (task.equals(UPDATE)) {
			//addBehaviour(new SourceUpdateBehaviour());
		}
	}

	
	
	
}
