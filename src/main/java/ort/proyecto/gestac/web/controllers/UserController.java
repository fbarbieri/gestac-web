package ort.proyecto.gestac.web.controllers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jade.wrapper.AgentContainer;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import net.sf.jade4spring.JadeBean;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.PruebaSpring;
import ort.proyecto.gestac.core.entities.Subject;
import ort.proyecto.gestac.core.entities.User;
import ort.proyecto.gestac.core.entities.repository.AreaDao;
import ort.proyecto.gestac.core.entities.repository.AreaDataSource;
import ort.proyecto.gestac.core.entities.repository.SujetoDao;
import ort.proyecto.gestac.core.entities.repository.UserDao;

//@Controller
public class UserController {

//	@Autowired
	private UserDao userDao;
//	@Autowired
	private AreaDao areaDao;
//	@Autowired
	private SujetoDao sujetoDao;
	
	@Autowired
	private PruebaSpring prueba;
	
	@Autowired
	private JadeBean jade;
	
//	@Autowired
	private AreaDataSource areaDataSource;

	
//	@RequestMapping("/contenedor")
	public String contenedor(){
		try{
//			Runtime jadeRuntime = Runtime.instance();
//			Profile profile = new ProfileImpl();
//			AgentContainer container = jadeRuntime.createMainContainer(profile);
			
//			System.out.println(container.getContainerName());
//			AgentController controlador = container.createNewAgent("agentedesdecodigo", "ort.proyecto.gestac.core.agentes.bd.AgenteBaseDeDatos", null);
//			controlador.start();
			
//			Agent agenteprueba = new ort.proyecto.gestac.core.agentes.bd.AgenteBaseDeDatos();
//			jade.getJadeControllerContainer().acceptNewAgentAndStart("agentedesdecodigoencontenedorspring", agenteprueba);
//			
//			System.out.println(jade.getJadeControllerContainer().getContainerName());
			
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		
		
		return "greeting2";
	}

//	@RequestMapping("/create")
//	@ResponseBody
	public String create(String name) {
		User user = null;
		try {
			user = new User(name);
//			userDao.save(user);
		} catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}
		return "User succesfully created! (id = " + user.getId() + ")";
	}

//	@RequestMapping("/delete")
//	@ResponseBody
	public String delete(long id) {
		try {
			User user = new User();
			user.setId(id);
//			userDao.delete(user);
		} catch (Exception ex) {
			return "Error deleting the user: " + ex.toString();
		}
		return "User succesfully deleted!";
	}

//	@RequestMapping("/get-by-name")
	public String getByEmail(String name) {
		String userId;
		try {
			User user = userDao.findByName(name);
			userId = String.valueOf(user.getId());
			
			Area area = new Area();
			area.setName("nombre de area");
			area.setDescription("descripcion de area");
			
			Subject sujeto = new Subject();
			sujeto.setName("nombre de sujeto");
			
			Set<Subject> list = new LinkedHashSet<Subject>();
			list.add(sujeto);
			area.setSubjects(list);
			sujeto.setArea(area);
			
//			areaDao.save(area);
//			sujetoDao.save(sujeto);
//			
//			Sujeto sdb = sujetoDao.findOne(1L);
//			Sujeto sdb2 = areaDao.findOne(1L).getSujetos().iterator().next();//get(0);
			
			System.out.println(area);
		} catch (Exception ex) {
			return "greeting2";
		}
		return "The user id is: " + userId;
	}

//	@RequestMapping("/update")
//	@ResponseBody
	public String updateUser(long id, String name) {
		try {
//			User user = userDao.findOne(id);
//			user.setName(name);
//			userDao.save(user);
		} catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return "User succesfully updated!";
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
