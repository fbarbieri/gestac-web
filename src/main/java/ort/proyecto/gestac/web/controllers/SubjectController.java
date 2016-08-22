package ort.proyecto.gestac.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Subject;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Subject> getAll() {
		List<Subject> result = new ArrayList<Subject>();
		List<Area> areas = interfaceAgent.getAreas();
		
		for (Area a : areas) {
			result.addAll(a.getSubjects());
		}
		
        return result;
    }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        if(subject.getName()==null) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        subject = interfaceAgent.addSubject(subject);
        if (subject!=null) {
        	return new ResponseEntity<Subject>(subject, HttpStatus.CREATED);          	
        } else {
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteSubject(@PathVariable("id") String id) {
		boolean success = interfaceAgent.deleteSubject(id);
		if (success) {
			return new ResponseEntity<Boolean>(success, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Boolean>(success, HttpStatus.CONFLICT);	
		}
	}
	
}
