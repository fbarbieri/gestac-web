package ort.proyecto.gestac.core.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Source {

	@Id 
	@GeneratedValue
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	private Area area;
	
	private String name;
	
	private String mail;
	
	private String userName;
	
	private String password;
	
	private Timestamp updated;
	
	private Timestamp evaluationUpdated;
	
	private boolean onUpdate;
	
	/**
	 * calificación
	 */
	
	private double workExperience;
	
	private double areaEducation;
	
	private double title;
	
	private double perceptionCommonSense;
	
	private double perceptionOrder;
	
	private double perceptionInterest;
	
	private double perceptionWorkCapacity;
	
	private double perceptionGroupWorkCapacity;
	
	private double perceptionTotal;

	private double ownEvaluationTotal;
	
	private double scoreTotal;

	
	public Source(String name) {
		super();
		this.name = name;
	}
	
	public Source() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Timestamp getEvaluationUpdated() {
		return evaluationUpdated;
	}

	public void setEvaluationUpdated(Timestamp evaluationUpdated) {
		this.evaluationUpdated = evaluationUpdated;
	}

	public boolean isOnUpdate() {
		return onUpdate;
	}

	public void setOnUpdate(boolean onUpdate) {
		this.onUpdate = onUpdate;
	}

	public double getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(double workExperience) {
		this.workExperience = workExperience;
	}

	public double getAreaEducation() {
		return areaEducation;
	}

	public void setAreaEducation(double areaEducation) {
		this.areaEducation = areaEducation;
	}

	public double getTitle() {
		return title;
	}

	public void setTitle(double title) {
		this.title = title;
	}

	public double getPerceptionCommonSense() {
		return perceptionCommonSense;
	}

	public void setPerceptionCommonSense(double perceptionCommonSense) {
		this.perceptionCommonSense = perceptionCommonSense;
	}

	public double getPerceptionOrder() {
		return perceptionOrder;
	}

	public void setPerceptionOrder(double perceptionOrder) {
		this.perceptionOrder = perceptionOrder;
	}

	public double getPerceptionInterest() {
		return perceptionInterest;
	}

	public void setPerceptionInterest(double perceptionInterest) {
		this.perceptionInterest = perceptionInterest;
	}

	public double getPerceptionWorkCapacity() {
		return perceptionWorkCapacity;
	}

	public void setPerceptionWorkCapacity(double perceptionWorkCapacity) {
		this.perceptionWorkCapacity = perceptionWorkCapacity;
	}

	public double getPerceptionGroupWorkCapacity() {
		return perceptionGroupWorkCapacity;
	}

	public void setPerceptionGroupWorkCapacity(double perceptionGroupWorkCapacity) {
		this.perceptionGroupWorkCapacity = perceptionGroupWorkCapacity;
	}

	public double getPerceptionTotal() {
		return perceptionTotal;
	}

	public void setPerceptionTotal(double perceptionTotal) {
		this.perceptionTotal = perceptionTotal;
	}

	public double getOwnEvaluationTotal() {
		return ownEvaluationTotal;
	}

	public void setOwnEvaluationTotal(double ownEvaluationTotal) {
		this.ownEvaluationTotal = ownEvaluationTotal;
	}

	public double getScoreTotal() {
		return scoreTotal;
	}

	public void setScoreTotal(double scoreTotal) {
		this.scoreTotal = scoreTotal;
	}
	
	
	
	
	
}
