package ort.proyecto.gestac.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	
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
	
//	@RequestMapping("/initialize")
	public String initialize() {
		try {
			
			Area area = new Area(1L, "area", "area", null, null, null);
			
//			Subject subject = new Subject(1L, "subject", area);
//			Set<Subject> subjects = new HashSet<>();
//			subjects.add(subject);
			
			areaRepository.save(area);
//			subjectRepository.save(subject);
			
			Area area2 = areaRepository.findOne(1L);
			System.out.println(area2);
			
//			area.setSubjects(subjects);
			
//			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//			String json = gson.toJson(area);
			
			String json = jsonMapper.writeValueAsString(area);
			
			System.out.println(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "greeting2";
	}
	
//	@RequestMapping("/search/areas")
	public String getAreas(){
		
		List<Area> areas = interfaceAgent.getAreas();
		
		for(Area a : areas) {
			System.out.println(a);
			for (Subject s : a.getSubjects()) {
				System.out.println(s);				
			}
		}
		
//		GuiEvent event = new GuiEvent(this, AgenteInterfaz.BUSCAR_TODAS_LAS_AREAS);
//        agenteInterfaz.postGuiEvent(event);
        
		
		return "greeting2";
	}
	
//	@RequestMapping("/search")
	public String search(){
		
		return "searchIssue";
	}
	
}
