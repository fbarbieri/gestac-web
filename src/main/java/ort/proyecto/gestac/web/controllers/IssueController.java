package ort.proyecto.gestac.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.NewKnowledgeRequestHolder;

@RestController
@RequestMapping("/issues")
public class IssueController {
	
	private Logger logger = LoggerFactory.getLogger(IssueController.class);
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public Issue createIssue(@RequestBody Issue issue) {
	
		if (issue!=null && issue.getTitle()!=null && !issue.getTitle().equals("") &&
				issue.getDescription()!=null && !issue.getDescription().equals("") &&
				issue.getSubjects()!=null && issue.getSubjects().size()>0 &&
				issue.getIncidents()!=null && issue.getIncidents().size()>0 &&
				issue.getGravity()!=null) {
			return interfaceAgent.addIssue(issue);
		} else {
			return null;
		}
		
	}

}
