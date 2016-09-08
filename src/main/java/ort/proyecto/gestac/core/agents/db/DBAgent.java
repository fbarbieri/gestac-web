package ort.proyecto.gestac.core.agents.db;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.AreaBestSource;
import ort.proyecto.gestac.core.entities.Gravity;
import ort.proyecto.gestac.core.entities.Incident;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Subject;
import ort.proyecto.gestac.core.entities.repository.AreaBestSourceRepository;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;
import ort.proyecto.gestac.core.entities.repository.GravityRepository;
import ort.proyecto.gestac.core.entities.repository.IncidentRepository;
import ort.proyecto.gestac.core.entities.repository.IssueSearchDataSource;
import ort.proyecto.gestac.core.entities.repository.SubjectRepository;

public class DBAgent extends GestacAgent {
	
	private Logger logger = LoggerFactory.getLogger(DBAgent.class);
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private IssueSearchDataSource issueSearch;
	
	@Autowired
	private IncidentRepository incidentRepository;
	
	@Autowired
	private GravityRepository gravityRepository;
	
	@Autowired
	private AreaBestSourceRepository areaBestSourceRepository;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	protected void setup() {
		super.setup();
		//Thread.currentThread().setContextClassLoader(classLoader);
		addBehaviour(new DBAgentBehaviour());
	}
	
	class DBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			//presentarse();
			
			ACLMessage message = blockingReceive();
            String content = message.getContent();
            String[] parameters = content.split("&");
            String operation = parameters[0];
            switch (operation){
            	case DBAgentOperations.FIND_ALL_AREAS:
            		List<Area> areas = areaRepository.findAllByOrderByNameAsc();
            		sendReply(areas, message);
            		break;
            	case DBAgentOperations.ADD_AREA:
					try {
						Area saved = areaRepository.save(jsonMapper.readValue(parameters[1], Area.class));
						AreaBestSource best = new AreaBestSource(saved.getId(), saved, null, new Timestamp(System.currentTimeMillis()));
						areaBestSourceRepository.save(best);
					} catch (IOException e) {
						logger.error("Error reading Area from json, area: " + parameters[1], e);
					}
            		break;
            	case DBAgentOperations.DELETE_AREA:
            		Area toDelete = areaRepository.findOne(Long.parseLong(parameters[1]));
            		if (toDelete.getIncidents().size()>0 || toDelete.getSources().size()>0 || 
            				toDelete.getSubjects().size()>0) {
            			sendReply(new Boolean(false), message);
            		} else {
            			areaRepository.delete(toDelete);
            			sendReply(new Boolean(true), message);
            		}
            		break;
            	case DBAgentOperations.ADD_SUBJECT:
            		addSubject(parameters[1], parameters[2], message);
            		break;
            	case DBAgentOperations.DELETE_SUBJECT:
            		deleteSubject(parameters[1], message);
            		break;
            	case DBAgentOperations.ADD_INCIDENT:
            		addIncident(parameters[1], parameters[2], message);
            		break;
            	case DBAgentOperations.DELETE_INCIDENT:
            		deleteIncident(parameters[1], message);
            		break;
            	case DBAgentOperations.ADD_GRAVITY:
            		addGravity(parameters[1], parameters[2], message);
            		break;
            	case DBAgentOperations.DELETE_GRAVITY:
            		deleteGravity(parameters[1], message);
            		break;
            }
			
			
		}
		
		private void addGravity(String jsonGravity, String incidentId, ACLMessage messageToReplyTo) {
			try {
				Gravity toSave = jsonMapper.readValue(jsonGravity, Gravity.class);
				if (gravityRepository.findByDescriptionIgnoreCaseAndIncidentId(toSave.getDescription(), 
						Long.parseLong(incidentId)).size()>0) {
					//ya existe
					sendEmptyReply(messageToReplyTo);
				} else {
					toSave.setIncident(new Incident(Long.parseLong(incidentId)));
					gravityRepository.save(toSave);
					sendReply(toSave, messageToReplyTo);
				}
			} catch (IOException e) {
				logger.error("Error parsing gravity, " + jsonGravity, e);
			}
		}

		private void deleteGravity(String id, ACLMessage messageToReplyTo) {
			try{
				List<Issue> uses = issueSearch.getIssuesByGravity(Long.parseLong(id));
				if (uses!=null && uses.size()>0) {
					sendReply(new Boolean(false), messageToReplyTo);
				} else {
					gravityRepository.delete(Long.parseLong(id));
					sendReply(new Boolean(true), messageToReplyTo);;					
				}
			} catch (Exception e) {
				logger.error("Error deleting Subject, id: " + id, e);
			}
		}

		private void addIncident(String jsonIncident, String areaId, ACLMessage messageToReplyTo) {
			try {
				Incident toSave = jsonMapper.readValue(jsonIncident, Incident.class);
				if (incidentRepository.findByNameIgnoreCaseAndAreaId(toSave.getName(), Long.parseLong(areaId)).size()>0) {
					//ya existe
					sendEmptyReply(messageToReplyTo);
				} else {
					toSave.setArea(new Area(Long.parseLong(areaId)));
					incidentRepository.save(toSave);
					sendReply(toSave, messageToReplyTo);
				}
			} catch (IOException e) {
				logger.error("Error parsing subject, " + jsonIncident, e);
			}
		}
		
		private void deleteIncident(String id, ACLMessage messageToReplyTo) {
			try{
				List<Issue> uses = issueSearch.getIssuesByIncident(Long.parseLong(id));
				if (uses!=null && uses.size()>0) {
					sendReply(new Boolean(false), messageToReplyTo);
				} else {
					Incident incident = incidentRepository.findOne(Long.parseLong(id));
					if (incident!=null && incident.getGravities()!=null && incident.getGravities().size()>0) {
						sendReply(new Boolean(false), messageToReplyTo);
					} else {
						incidentRepository.delete(Long.parseLong(id));
						sendReply(new Boolean(true), messageToReplyTo);;					
					}
				}
			} catch (Exception e) {
				logger.error("Error deleting Subject, id: " + id, e);
			}
		}
		
		private void deleteSubject(String id, ACLMessage messageToReplyTo) {
			try{
				List<Issue> uses = issueSearch.getIssuesBySubject(Long.parseLong(id));
				if (uses!=null && uses.size()>0) {
					sendReply(new Boolean(false), messageToReplyTo);
				} else {
					subjectRepository.delete(Long.parseLong(id));
					sendReply(new Boolean(true), messageToReplyTo);;					
				}
			} catch (Exception e) {
				logger.error("Error deleting Subject, id: " + id, e);
			}
		}

		private void addSubject(String jsonSubject, String areaId, ACLMessage messageToReplyTo) {
			try {
				Subject toSave = jsonMapper.readValue(jsonSubject, Subject.class);
				if (subjectRepository.findByNameIgnoreCaseAndAreaId(toSave.getName(), Long.parseLong(areaId)).size()>0) {
					//ya existe
					sendEmptyReply(messageToReplyTo);
				} else {
					toSave.setArea(new Area(Long.parseLong(areaId)));
					subjectRepository.save(toSave);
					sendReply(toSave, messageToReplyTo);
				}
			} catch (IOException e) {
				logger.error("Error parsing subject, " + jsonSubject, e);
			}
		}
		
		private void presentarse() {
			try {
				System.out.println("Agente base de datos, " + this + ", " + this.myAgent.getName() + ", " + this.myAgent.getContainerController().getContainerName());
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
}
