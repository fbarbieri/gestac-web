package ort.proyecto.gestac.web.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.entities.Subject;
import ort.proyecto.gestac.core.entities.repository.SubjectRepository;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

	@Autowired
	private SubjectRepository subjectRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Subject> getAll() {
        return subjectRepository.findAll();
    }
	
}
