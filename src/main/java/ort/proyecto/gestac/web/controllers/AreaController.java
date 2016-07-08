package ort.proyecto.gestac.web.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;

@RestController
@RequestMapping("/areas")
public class AreaController {
	
	@Autowired
	private InterfaceAgent interfaceAgent;
	@Autowired
	private AreaRepository areaRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Area> getAll() {
//		List<Area> list = areaRepository.findAll();
//		for (Area a : list) {
//			System.out.println(a);
//		}
		List<Area> areas = interfaceAgent.getAreas();
		return areas;
    }

}
