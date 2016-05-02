package planner.heuristics;

import java.util.ArrayList;
import java.util.List;

import planner.core.*;
import search.core.BestFirstHeuristic;

public class PEGS_FILLED implements BestFirstHeuristic<PlanStep> {

	private List<String> holes;

	@Override
	public int getDistance(PlanStep node, PlanStep goal) {
		Domain dom = node.getDomain();
		//System.out.println("Domain: " + dom.getName());
		if(!dom.getName().equals("pegs")){
			return 0;
		}
		
		if(holes == null){
			holes = new ArrayList<String>();
			setup(node.getProblem());
		}
		
		return calculate(node.getWorldState());
	}
	
	private void setup(Problem prob){
		State start = prob.getStartState();

		for(Predicate p: start){
			String peg = p.getParams().get(0);
			if(!holes.contains(peg)){
				holes.add(peg);
			}
		}
	}

	private int calculate(State currentState){
		ArrayList<String> empties = new ArrayList<String>();
		for(Predicate p: currentState){
			String name = p.getName();
			switch(name){
				case "empty":
					String temp0 = p.getParams().get(0);
					empties.add(temp0);
					break;
			}
		}
		return holes.size() - empties.size();
	}

}
