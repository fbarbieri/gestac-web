package ort.proyecto.gestac.core.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class AgenteDosComportamientos extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		System.out.println(this.toString() + " - setup");
		addBehaviour(new BehaviourUno());
		System.out.println(this.toString() + " - comportamiento uno agregado");
		addBehaviour(new BehaviourDos());
		System.out.println(this.toString() + " - comportamiento dos agregado");
	}
	
	class BehaviourUno extends CyclicBehaviour {

		@Override
		public void action() {
			try {
				System.out.println(super.toString() + " - comportamiento uno");
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

	}
	
	class BehaviourDos extends CyclicBehaviour {

		@Override
		public void action() {
			try {
				System.out.println(super.toString() + " - comportamiento dos");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

	}
	
}
