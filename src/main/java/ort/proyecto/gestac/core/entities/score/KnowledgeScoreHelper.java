package ort.proyecto.gestac.core.entities.score;

import java.math.BigDecimal;

import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;

public class KnowledgeScoreHelper {

	public static double calculateScore(Knowledge knowledge) {
		double totalEvaluations = knowledge.getEvaluations().size();
		double finalScore = 0;
		
		for (KnowledgeEvaluation evaluation : knowledge.getEvaluations()) {
			finalScore += 
					(evaluation.getSimplicity() + 
					evaluation.getUsedTime() +
					evaluation.getReuse()) / 3;
		}
		
		finalScore = finalScore / totalEvaluations;
		
		return new BigDecimal(finalScore).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

}
