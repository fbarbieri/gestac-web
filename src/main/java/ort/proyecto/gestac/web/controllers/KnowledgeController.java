package ort.proyecto.gestac.web.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeController.class);
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	
	@RequestMapping(value = "/bestForIssue/{issueId}", method = RequestMethod.GET)
	public ResponseEntity<Knowledge> getBestKnowledgeForIssue(@PathVariable("issueId") String issueId) {
		Knowledge best = interfaceAgent.getBestKnowledge(issueId);
		if (best!=null) {
			return new ResponseEntity<Knowledge>(best, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<Knowledge>(HttpStatus.NO_CONTENT);
		}
	}
	
	@RequestMapping(value = "/allForIssue/{issueId}", method = RequestMethod.GET)
	public ResponseEntity<List<Knowledge>> getAllKnowledgesForIssue(@PathVariable("issueId") String issueId) {
		List<Knowledge> all = interfaceAgent.getAllKnowledgesForIssue(issueId);
		if (all!=null) {
			return new ResponseEntity<List<Knowledge>>(all, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<List<Knowledge>>(HttpStatus.NO_CONTENT);
		}
	}
	
	@RequestMapping(value = "/newKnowledge", method = RequestMethod.POST)
    public Knowledge newKnowledge(@RequestBody NewKnowledgeRequestHolder holder) {
		Knowledge toSave = holder.getKnowledge();
		toSave.setIssue(holder.getIssue());
		toSave.setSource(holder.getSource());
		toSave.setKnowledgeScore(0);
		toSave.setTotalEvaluations(0);
		toSave.setConsideredEvaluations(0);
		
		Long id = interfaceAgent.addKnowledge(toSave);
		toSave.setId(id);
		holder.getEvaluation().setKnowledge(toSave);
		holder.getEvaluation().setDate(new Timestamp(System.currentTimeMillis()));
		interfaceAgent.addKnowledgeEvaluation(holder.getEvaluation());
		System.out.println("");
		
		return toSave;
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
