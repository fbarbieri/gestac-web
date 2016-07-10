package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ort.proyecto.gestac.core.entities.Issue;


public interface IssueRepository extends JpaRepository<Issue, Long>{
	
}
