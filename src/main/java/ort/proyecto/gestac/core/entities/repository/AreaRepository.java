package ort.proyecto.gestac.core.entities.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ort.proyecto.gestac.core.entities.Area;

public interface AreaRepository extends JpaRepository<Area, Long>{

	Optional<Area> findByName(String name);
	
	List<Area> findAllByOrderByNameAsc();
	
}
