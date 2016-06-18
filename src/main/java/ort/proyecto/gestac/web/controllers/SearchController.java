package ort.proyecto.gestac.web.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.AreaDataSource;
import ort.proyecto.gestac.core.entities.AreaRepository;
import ort.proyecto.gestac.core.entities.Subject;
import ort.proyecto.gestac.core.entities.SubjectRepository;

@Controller
public class SearchController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	
	
	@RequestMapping("/initialize")
	public String initialize() {
		try {
			
			Area area = new Area(1L, "area", "area", null);
			
			Subject subject = new Subject(1L, "subject", area);
			Set<Subject> subjects = new HashSet<>();
			subjects.add(subject);
			
			areaRepository.save(area);
			subjectRepository.save(subject);
			
			Area area2 = areaRepository.findOne(1L);
			System.out.println(area2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "greeting2";
	}
	
	@RequestMapping("/search/areas")
	public String getAreas(){
		
		interfaceAgent.getAreas();
		
//		GuiEvent event = new GuiEvent(this, AgenteInterfaz.BUSCAR_TODAS_LAS_AREAS);
//        agenteInterfaz.postGuiEvent(event);
        
		
		return "greeting2";
	}
	
}
