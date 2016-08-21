package ort.proyecto.gestac.web.controllers;

import java.net.InterfaceAddress;
import java.sql.Timestamp;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Source;
import ort.proyecto.gestac.core.entities.score.SourceScoreHelper;

@RestController
@RequestMapping("/sources")
public class SourceController {
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	
	@RequestMapping(value = "/getIssuesWithKnowledge/{sourceId}/{areaId}", method = RequestMethod.GET)
	public Collection<Issue> getIssuesWithKnowledge(@PathVariable("sourceId") String sourceId, 
			@PathVariable("areaId") String areaId) {
		if(sourceId==null || sourceId.equals("") || areaId==null || areaId.equals("")) {
        	return null;
        }
        
        List<Issue> issuesToAnswer = interfaceAgent.getIssuesWithKnowledgeForSourceArea(sourceId, areaId);
        
        return issuesToAnswer;
	}
	
	@RequestMapping(value = "/isBestSource/{sourceId}", method = RequestMethod.GET)
    public Collection<Issue> isBestSource(@PathVariable("sourceId") String sourceId) {
        if(sourceId==null || sourceId.equals("")) {
        	return null;
        }
        
        List<Issue> issuesToAnswer = interfaceAgent.isBestSourceForHisArea(sourceId);
        
        return issuesToAnswer;
        
//        if (!interfaceAgent.sourceExists(source)) {
//        	SourceScoreHelper.calculateAndSetEvaluationTotal(source);
//        	source.setScoreTotal(source.getOwnEvaluationTotal());
//        	Timestamp updated = new Timestamp(System.currentTimeMillis());
//        	source.setUpdated(updated);
//        	source.setEvaluationUpdated(updated);
//        	
//        	interfaceAgent.saveSource(source);
//        } else {
//        	return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
  
  
//        return new ResponseEntity<Source>(source, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Source> createSource(@RequestBody Source source) {
        if(source.getName()==null)
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
        
        if (!interfaceAgent.sourceExists(source)) {
        	source.setPerceptionTotal(SourceScoreHelper.calculatePerceptionTotal(source));
        	source.setOwnEvaluationTotal(SourceScoreHelper.calculateOwnEvaluationTotal(source));
        	source.setScoreTotal(source.getOwnEvaluationTotal());
        	Timestamp updated = new Timestamp(System.currentTimeMillis());
        	source.setUpdated(updated);
        	source.setEvaluationUpdated(updated);
        	
        	interfaceAgent.saveSource(source);
        } else {
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
  
  
        return new ResponseEntity<Source>(source, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteSource(@PathVariable("id") String id) {
		boolean success = interfaceAgent.deleteSource(id);
		if (success) {
			return new ResponseEntity<Boolean>(success, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Boolean>(success, HttpStatus.CONFLICT);	
		}
    }
	
	@RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<Source> updateSource(@RequestBody Source source) {
        if(source.getName()==null)
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
        
        if (interfaceAgent.sourceExists(source)) {
        	source.setPerceptionTotal(SourceScoreHelper.calculatePerceptionTotal(source));
        	source.setOwnEvaluationTotal(SourceScoreHelper.calculateOwnEvaluationTotal(source));
        	Timestamp updated = new Timestamp(System.currentTimeMillis());
        	source.setUpdated(updated);
        	source.setEvaluationUpdated(updated);
        	
        	interfaceAgent.saveSource(source);
        } else {
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
  
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
