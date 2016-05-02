package planner.heuristics;

import planner.core.*;
import search.core.BestFirstHeuristic;

public class RelaxedPlan implements BestFirstHeuristic<PlanStep> {

	@Override
	public int getDistance(PlanStep node, PlanStep goal) {
		Problem prob = node.getProblem();
		State currentState = node.getWorldState();
		Domain dom = node.getDomain();
		PlanGraph pg = new PlanGraph(dom, currentState, prob);
		Plan noDelete = pg.extractNoDeletePlan();
		int numSteps = noDelete.length();
		//System.out.println("Step Count: " + numGoals);
		return numSteps;
	}

}
