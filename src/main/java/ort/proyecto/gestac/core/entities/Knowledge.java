package ort.proyecto.gestac.core.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//import com.google.gson.annotations.Expose;

@Entity
public class Knowledge {

//	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
}
