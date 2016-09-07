package ort.proyecto.gestac.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Subject;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;
import ort.proyecto.gestac.core.entities.repository.SubjectRepository;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public ResponseEntity<String> test(){
		return new ResponseEntity<String>("",HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/getIssuesForAreaSubjectIncidentGravity/{areaId}/{subjectId}/{incidentId}/{gravityId}", method = RequestMethod.GET)
	public Collection<Issue> getIssuesForAreaSubjectIncidentGravity(
			@PathVariable("areaId") String areaId, 
			@PathVariable("subjectId") String subjectId, 
			@PathVariable("incidentId") String incidentId, 
			@PathVariable("gravityId") String gravityId) {

		List<Issue> issues = new ArrayList<Issue>();
//		issues.add(new Issue());
		issues = interfaceAgent.findIssues(areaId, subjectId, incidentId, gravityId);
		
		return issues;
	}
	
}
