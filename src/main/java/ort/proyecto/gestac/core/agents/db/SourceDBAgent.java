package ort.proyecto.gestac.core.agents.db;

import org.springframework.beans.factory.annotation.Autowired;

import jade.core.Agent;
import ort.proyecto.gestac.core.entities.repository.SourceRepository;

public class SourceDBAgent extends Agent {
	
	@Autowired
	private SourceRepository sourceRepository;

}
