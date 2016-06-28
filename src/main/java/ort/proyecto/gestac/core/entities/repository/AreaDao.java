package ort.proyecto.gestac.core.entities.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import ort.proyecto.gestac.core.entities.Area;


//@Transactional
public interface AreaDao /*extends CrudRepository<Area, Long> */{

	public Area findByNombre(String name);
	
}
