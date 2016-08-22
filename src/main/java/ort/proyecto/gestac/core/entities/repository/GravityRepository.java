package ort.proyecto.gestac.core.entities.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import ort.proyecto.gestac.core.entities.Gravity;
import ort.proyecto.gestac.core.entities.Incident;

public interface GravityRepository extends JpaRepository<Gravity, Long>{

	Collection<Gravity> findByDescriptionIgnoreCaseAndIncidentId(String description, Long incidentId);
	
}
