package ort.proyecto.gestac.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ort.proyecto.gestac.core.agents.InterfaceAgent;
import ort.proyecto.gestac.core.entities.Source;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private InterfaceAgent interfaceAgent;
	
	
	@RequestMapping(value = "/newKnowledgeLogin", method = RequestMethod.POST)
    public ResponseEntity<Source> login(@RequestBody Source source) {
		System.out.println(source);
		Source loggedSource = null;
		if (source.getUserName()!=null && source.getPassword()!=null) {
			loggedSource = interfaceAgent.loginSource(source.getUserName(), source.getPassword());
		}
		return new ResponseEntity<Source>(loggedSource, HttpStatus.ACCEPTED);
	}
	
}
