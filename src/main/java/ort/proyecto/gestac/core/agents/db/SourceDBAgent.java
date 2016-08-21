package ort.proyecto.gestac.core.agents.db;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Source;
import ort.proyecto.gestac.core.entities.repository.SourceDataSource;
import ort.proyecto.gestac.core.entities.repository.SourceRepository;

public class SourceDBAgent extends GestacAgent {

	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(SourceDBAgent.class);

	@Autowired
	private SourceRepository sourceRepository;
	
	@Autowired
	private SourceDataSource sourceDataSource;
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new SourceDBAgentBehaviour());
	}
	
	class SourceDBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				String content = message.getContent();
	            String[] parameters = content.split("&");
	            String operation = parameters[0];
	            switch (operation){
	            case DBAgentOperations.FIND_ALL_SOURCES:
	        		List<Source> allSources = sourceRepository.findAllByOrderByNameAsc();
	            	sendReply(allSources, message);
	        		break;
	            case DBAgentOperations.FIND_SOURCE_BY_USER:
	            	Source source = sourceRepository.findByUserName(parameters[1]);
	            	sendReply(source, message);
	            	break;
	            case DBAgentOperations.SAVE_SOURCE:
	            	Source saved = null;
	            	try {
	            		Source toSave = getJsonMapper().readValue(parameters[1], Source.class);
	            		toSave.setArea(new Area(Long.parseLong(parameters[2])));
						saved = sourceRepository.save(toSave);
						sourceDataSource.addAsBestIfNull(saved);
					} catch (IOException e) {
						logger.error("Error saving source " + parameters[1], e);
					}
	            	if (saved!=null){
	            		sendReply(DBAgentOperations.OK, message);
	            	} else {
	            		sendReply(DBAgentOperations.ERROR, message);
	            	}
	            	break;
	            case DBAgentOperations.GET_SOURCE_BY_LOGIN:
	            	Source login = sourceRepository.findByUserNameAndPassword(parameters[1], parameters[2]);
	            	sendReply(login, message);
	            	break;
	            case DBAgentOperations.GET_AREA_FOR_BEST_SOURCE:
	            	Area bestArea = sourceDataSource.getAreaForBestSource(Long.parseLong(parameters[1]));
	            	sendReply(bestArea, message);
	            	break;
	            case DBAgentOperations.UPDATE_SOURCE_EVALUATION:
            		Source sourceFromDb = sourceRepository.findOne(Long.parseLong(parameters[1]));
            		sourceFromDb.setScoreTotal(Double.parseDouble(parameters[2]));
            		sourceFromDb.setEvaluationUpdated(new Timestamp(Long.parseLong(parameters[3])));
					sourceRepository.save(sourceFromDb);
	            	break;
	            case DBAgentOperations.SEARCH_AND_UPDATE_BEST_SOURCE_FOR_AREA:
	            	sourceDataSource.updateBestSourceForArea();
	            	break;
	            case DBAgentOperations.DELETE_SOURCE:
	            	sendReply(doDelete(parameters[1]), message);
	            	break;
	            }
			} else {
				block();
			}
		}
		
		public Boolean doDelete(String id) {
			Long idToDelete = Long.parseLong(id);
			if (sourceDataSource.isDeletable(idToDelete)) {
				sourceRepository.delete(idToDelete);
				return true;
			} else {
				return false;
			}
		}
		
		
	}

}
