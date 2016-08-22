package ort.proyecto.gestac.core.entities.repository;

import ort.proyecto.gestac.core.entities.User;

//@Transactional
public interface UserDao /*extends CrudRepository<User, Long>*/{
	
	public User findByName(String name);

}
