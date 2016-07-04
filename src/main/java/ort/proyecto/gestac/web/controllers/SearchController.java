package ort.proyecto.gestac.web.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Subject;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;
import ort.proyecto.gestac.core.entities.repository.SubjectRepository;

//@Controller
public class SearchController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	
	
//	@RequestMapping("/initialize")
	public String initialize() {
		try {
			
			Area area = new Area(1L, "area", "area", null, null, null);
			
			Subject subject = new Subject(1L, "subject", area);
			Set<Subject> subjects = new HashSet<>();
			subjects.add(subject);
			
			areaRepository.save(area);
			subjectRepository.save(subject);
			
			Area area2 = areaRepository.findOne(1L);
			System.out.println(area2);
			
			area.setSubjects(subjects);
			
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(area);
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
