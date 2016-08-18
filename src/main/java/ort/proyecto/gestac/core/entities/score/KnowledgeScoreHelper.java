package ort.proyecto.gestac.core.entities.score;

import java.math.BigDecimal;
import java.util.Set;

import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;

public class KnowledgeScoreHelper {

	public static double calculateScore(Knowledge knowledge) {
		return calculateScore(knowledge.getEvaluations());
	}
	
	public static double calculateScore(Set<KnowledgeEvaluation> evaluations) {
		double totalEvaluations = evaluations.size();
		double finalScore = 0;
		
		for (KnowledgeEvaluation evaluation : evaluations) {
			finalScore += 
					(evaluation.getSimplicity() + 
					evaluation.getUsedTime() +
					evaluation.getReuse()) / 3;
		}
		
		finalScore = finalScore / totalEvaluations;
		
		return new BigDecimal(finalScore).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	public static double calculatePartialScore(KnowledgeEvaluation evaluation) {
		double result = (evaluation.getSimplicity() + 
				evaluation.getUsedTime() +
				evaluation.getReuse()) / 3;
		return new BigDecimal(result).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

}
