package ort.proyecto.gestac.core.entities.score;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;
import ort.proyecto.gestac.core.entities.Source;

public class SourceScoreHelper {

	public static double calculatePerceptionTotal(Source source) {
		double perception = 0;
		perception = (source.getPerceptionCommonSense() + source.getPerceptionOrder() + 
				source.getPerceptionInterest() + source.getPerceptionWorkCapacity() + 
				source.getPerceptionGroupWorkCapacity()) / 5;
		//return new BigDecimal(perception).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		return new BigDecimal(perception).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	public static double calculateOwnEvaluationTotal(Source source) {
		double own = 0;
		own = (source.getWorkExperience() + source.getAreaEducation() + 
				source.getTitle() + source.getPerceptionTotal()) / 4;
		return new BigDecimal(own).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

	public static double calculateTotalEvaluation(Source source, Set<KnowledgeEvaluation> evaluations) {
		double total = 0;
		
		List<KnowledgeEvaluation> evaluationsList = new ArrayList<>(evaluations); 
		Collections.sort(evaluationsList, new Comparator<KnowledgeEvaluation>(){
			@Override
			public int compare(KnowledgeEvaluation evaluation1, KnowledgeEvaluation evaluation2) {
				if (evaluation1.getDate().before(evaluation2.getDate())) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		KnowledgeEvaluation sourceKnowledgeEvaluation = evaluationsList.get(0);
		
		total = (source.getOwnEvaluationTotal() * KnowledgeScoreHelper.calculateScore(evaluations)) / 
					KnowledgeScoreHelper.calculatePartialScore(sourceKnowledgeEvaluation);
		
		return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
}
