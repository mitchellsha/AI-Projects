package planner.heuristics;

import planner.core.*;
import search.core.BestFirstHeuristic;

public class CountGoals implements BestFirstHeuristic<PlanStep> {

	@Override
	public int getDistance(PlanStep node, PlanStep goal) {
		State worldState1 = node.getWorldState();
		State worldState2 = goal.getWorldState();
		int numGoals = worldState1.unmetGoals(worldState2).size();
		//System.out.println("Goal Count: " + numGoals);
		return numGoals;
	}

}
