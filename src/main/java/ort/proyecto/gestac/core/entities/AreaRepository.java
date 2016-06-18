package ort.proyecto.gestac.core.entities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long>{

	Optional<Area> findByName(String name);
	
}
