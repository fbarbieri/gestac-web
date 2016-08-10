package ort.proyecto.gestac.core.entities.score;

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
		
		return finalScore;
	}

}
