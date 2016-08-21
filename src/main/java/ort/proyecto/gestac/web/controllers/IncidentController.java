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
import ort.proyecto.gestac.core.entities.Incident;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Incident> getAll() {
		List<Incident> result = new ArrayList<Incident>();
		List<Area> areas = interfaceAgent.getAreas();
		
		for (Area a : areas) {
			result.addAll(a.getIncidents());
		}
		
        return result;
    }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        if(incident.getName()==null) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        incident = interfaceAgent.addIncident(incident);
        if (incident!=null) {
        	return new ResponseEntity<Incident>(incident, HttpStatus.CREATED);          	
        } else {
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteIncident(@PathVariable("id") String id) {
		boolean success = interfaceAgent.deleteIncident(id);
		if (success) {
			return new ResponseEntity<Boolean>(success, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Boolean>(success, HttpStatus.CONFLICT);	
		}
	}
	
}
