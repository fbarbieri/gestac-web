package ort.proyecto.gestac.core.entities.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import ort.proyecto.gestac.core.entities.User;

//@Transactional
public interface UserDao /*extends CrudRepository<User, Long>*/{
	
	public User findByName(String name);

}
