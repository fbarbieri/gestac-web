package ort.proyecto.gestac.core.entities;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long>{

	Collection<Subject> findByAreaName(String name);
	
}
