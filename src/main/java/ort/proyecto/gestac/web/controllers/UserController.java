package ort.proyecto.gestac.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ort.proyecto.gestac.core.entidades.Area;
import ort.proyecto.gestac.core.entidades.AreaDao;
import ort.proyecto.gestac.core.entidades.PruebaSpring;
import ort.proyecto.gestac.core.entidades.Sujeto;
import ort.proyecto.gestac.core.entidades.SujetoDao;
import ort.proyecto.gestac.core.entidades.User;
import ort.proyecto.gestac.core.entidades.UserDao;

@Controller
public class UserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private SujetoDao sujetoDao;
	
	@Autowired
	private PruebaSpring prueba;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping("/create")
	@ResponseBody
	public String create(String name) {
		User user = null;
		try {
			user = new User(name);
			userDao.save(user);
		} catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}
		return "User succesfully created! (id = " + user.getId() + ")";
	}

	@RequestMapping("/delete")
	@ResponseBody
	public String delete(long id) {
		try {
			User user = new User();
			user.setId(id);
			userDao.delete(user);
		} catch (Exception ex) {
			return "Error deleting the user: " + ex.toString();
		}
		return "User succesfully deleted!";
	}

	@RequestMapping("/get-by-name")
	public String getByEmail(String name) {
		String userId;
		try {
			User user = userDao.findByName(name);
			userId = String.valueOf(user.getId());
			
			Area area = new Area();
			area.setNombre("nombre de area");
			area.setDescripcion("descripcion de area");
			
			Sujeto sujeto = new Sujeto();
			sujeto.setName("nombre de sujeto");
			
			List<Sujeto> list = new ArrayList<Sujeto>();
			list.add(sujeto);
			area.setSujetos(list);
			sujeto.setArea(area);
			
			areaDao.save(area);
			sujetoDao.save(sujeto);
			
			Sujeto sdb = sujetoDao.findOne(1L);
			Sujeto sdb2 = areaDao.findOne(1L).getSujetos().get(0);
			
			System.out.println(area);
		} catch (Exception ex) {
			return "greeting2";
		}
		return "The user id is: " + userId;
	}

	@RequestMapping("/update")
	@ResponseBody
	public String updateUser(long id, String name) {
		try {
			User user = userDao.findOne(id);
			user.setName(name);
			userDao.save(user);
		} catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return "User succesfully updated!";
	}

}
