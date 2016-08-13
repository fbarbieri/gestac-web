package ort.proyecto.gestac.core.entities.score;

import java.math.BigDecimal;

import ort.proyecto.gestac.core.entities.Source;

public class SourceScoreHelper {

	public static void calculateAndSetEvaluationTotal(Source source) {
		double perception = 0;
		perception = (source.getPerceptionCommonSense() + source.getPerceptionOrder() + 
				source.getPerceptionInterest() + source.getPerceptionWorkCapacity() + 
				source.getPerceptionGroupWorkCapacity()) / 5;
		source.setPerceptionTotal(perception);
		
		double total = 0;
		total = (source.getWorkExperience() + source.getAreaEducation() + 
				source.getTitle() + source.getPerceptionTotal()) / 4;
		source.setOwnEvaluationTotal(total);
				
	}
	
}
