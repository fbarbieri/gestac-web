package ort.proyecto.gestac.core.entidades;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;


@Transactional
public interface AreaDao extends CrudRepository<Area, Long> {

	public Area findByNombre(String name);
	
}
