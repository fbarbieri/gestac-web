package ort.proyecto.gestac.core.entities.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import ort.proyecto.gestac.core.entities.Incident;


public interface IncidentRepository  extends JpaRepository<Incident, Long>{

	Collection<Incident> findByNameIgnoreCaseAndAreaId(String name, Long id);
	
}
