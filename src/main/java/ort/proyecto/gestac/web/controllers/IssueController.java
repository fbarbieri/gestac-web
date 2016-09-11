package ort.proyecto.gestac.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Issue;

@RestController
@RequestMapping("/issues")
public class IssueController {
	
	private Logger logger = LoggerFactory.getLogger(IssueController.class);
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {
	
		if (issue!=null && issue.getTitle()!=null && !issue.getTitle().equals("") &&
				issue.getDescription()!=null && !issue.getDescription().equals("") &&
				issue.getSubjects()!=null && issue.getSubjects().size()>0 &&
				issue.getIncidents()!=null && issue.getIncidents().size()>0 &&
				issue.getGravity()!=null) {
			Issue saved = interfaceAgent.addIssue(issue);
			if (saved!=null) {
				return new ResponseEntity<Issue>(saved, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<Issue>(HttpStatus.BAD_REQUEST); 
			}
		} else {
			return new ResponseEntity<Issue>(HttpStatus.BAD_REQUEST);
		}
		
	}

}
