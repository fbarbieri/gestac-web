package ort.proyecto.gestac.core.entities.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ort.proyecto.gestac.core.entities.Source;

public interface SourceRepository extends JpaRepository<Source, Long> {

	Optional<Source> findByName(String name);
	
	List<Source> findAllByOrderByNameAsc();
	
	Source findByNameAndMail(String name, String mail);
	
	Source findByUserNameAndPassword(String userName, String password);
	
}
