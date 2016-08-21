package ort.proyecto.gestac.core.entities.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{

	Collection<Subject> findByAreaName(String name);
	
	Collection<Subject> findByNameIgnoreCaseAndAreaId(String name, Long id);
	
}
