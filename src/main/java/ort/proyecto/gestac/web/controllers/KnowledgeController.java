package ort.proyecto.gestac.web.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	@RequestMapping(value = "/bestForIssue/{issueId}", method = RequestMethod.GET)
	public Knowledge getBestKnowledgeForIssue(@PathVariable("issueId") String issueId) {
		
		Knowledge best = interfaceAgent.getBestKnowledge(issueId);
		
		return best;
		
	}
	
	@RequestMapping(value = "/addEvaluationToKnowledge/{knowledgeId}/{simplicity}/{usedTime}/{reuse}", method = RequestMethod.GET)
	public boolean addEvaluationToKnowledge(@PathVariable("knowledgeId") String knowledgeId, 
			@PathVariable("simplicity") String simplicity, 
			@PathVariable("usedTime") String usedTime, 
			@PathVariable("reuse") String reuse) {
		
		if (simplicity.equals("undefined") || usedTime.equals("undefined") || reuse.equals("undefined") || knowledgeId==null) {
			return false;
		} else {
			try {
				KnowledgeEvaluation evaluation = new KnowledgeEvaluation(new Knowledge(Long.parseLong(knowledgeId)), 
						new Timestamp(System.currentTimeMillis()), Double.parseDouble(simplicity), 
						Double.parseDouble(usedTime), Double.parseDouble(reuse));
				interfaceAgent.addKnowledgeEvaluation(evaluation);
			} catch(NumberFormatException e) {
				return false;
			}
		}
		
//		KnowledgeEvaluation evaluation = new KnowledgeEvaluation(new Knowledge(), new Timestamp(System.currentTimeMillis()), 
//				simplicity, usedTime, reuse);
		
		return true;
		
	}

}
