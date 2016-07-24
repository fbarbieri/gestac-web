package ort.proyecto.gestac.core.agents;

import java.util.Arrays;
import java.util.List;

public class KnowledgeAgent extends GestacAgent {

	private static final long serialVersionUID = 1L;

	private List<String> modes;
	
	//un comportamiento que reciba la consulta de mejor conocimiento.
	//aparte, el conocimiento que hace refresh
	//y el otro que hace update.

	public KnowledgeAgent() {
		super();
	}
	
	public KnowledgeAgent(String... modes) {
		super();
		this.modes = Arrays.asList(modes);
	}
	
	@Override
	protected void setup() {
		if (modes!=null && modes.size()>0) {
			if (modes.contains("refresh")) {
//				addBehaviour(new RefreshKnowledgeScoreBehaviour());
			}
			if (modes.contains("query")) {
				
			}
//			if (mode.equals("update")) {
//				addBehaviour(b);
//			}
		}
	}
	
	

}
