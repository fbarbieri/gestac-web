package ort.proyecto.gestac.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Knowledge;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	@RequestMapping(value = "/bestForIssue/{issueId}", method = RequestMethod.GET)
	public Knowledge getBestKnowledgeForIssue(@PathVariable("issueId") String issueId) {
		
//		Knowledge best = interfaceAgent.getBestKnowledge(issueId);
		
//		return best;
		return null;
	}

}
