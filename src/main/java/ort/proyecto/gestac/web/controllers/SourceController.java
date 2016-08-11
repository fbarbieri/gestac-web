package ort.proyecto.gestac.web.controllers;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Source;

@RestController
@RequestMapping("/sources")
public class SourceController {
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Source> createUser(@RequestBody Source source) {
        if(source.getName()==null)
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
//        if (areas.containsKey(area.getName()))
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        
//        areas.put(area.getName(),area);
  
        return new ResponseEntity<Source>(source, HttpStatus.CREATED);
    }

	@RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Source> getAll() {
		//List<Source> sources = new ArrayList<Source>();//interfaceAgent.getAreas();
		//sources.add(new Source("fuente para probar la grilla..."));
		
		List<Source> sources = interfaceAgent.getSources();
		return sources;
    }
	
}
