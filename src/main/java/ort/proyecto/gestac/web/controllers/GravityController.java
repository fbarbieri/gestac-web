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
import ort.proyecto.gestac.core.entities.Gravity;
import ort.proyecto.gestac.core.entities.Incident;

@RestController
@RequestMapping("/gravities")
public class GravityController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Gravity> getAll() {
		List<Gravity> result = new ArrayList<Gravity>();
		List<Area> areas = interfaceAgent.getAreas();
		
		for (Area a : areas) {
			for (Incident i : a.getIncidents())		
				result.addAll(i.getGravities());
		}
		
        return result;
    }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Gravity> createGravity(@RequestBody Gravity gravity) {
        if(gravity.getDescription()==null) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        gravity = interfaceAgent.addGravity(gravity);
        if (gravity!=null) {
        	return new ResponseEntity<Gravity>(gravity, HttpStatus.CREATED);          	
        } else {
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteGravity(@PathVariable("id") String id) {
		boolean success = interfaceAgent.deleteGravity(id);
		if (success) {
			return new ResponseEntity<Boolean>(success, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Boolean>(success, HttpStatus.CONFLICT);	
		}
	}
	
}
