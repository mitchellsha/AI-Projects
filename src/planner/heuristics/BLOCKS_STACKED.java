package planner.heuristics;

import java.util.HashMap;
import planner.core.*;
import search.core.BestFirstHeuristic;

public class BLOCKS_STACKED implements BestFirstHeuristic<PlanStep> {

	private HashMap<String, String> stacks;

	@Override
	public int getDistance(PlanStep node, PlanStep goal) {
		Domain dom = node.getDomain();
		//System.out.println("Domain: " + dom.getName());
		if(!dom.getName().equals("blocks")){
			return 0;
		}
		
		if(stacks == null){
			stacks = new HashMap<String, String>();
			setup(node.getProblem());
		}
		
		return calculate(node.getWorldState());
	}
	
	private void setup(Problem prob){
		State goal = prob.getGoals();

		for(Predicate p: goal){
			String block0 = p.getParams().get(0);
			String block1 = p.getParams().get(1);
			if(!stacks.containsKey(block0)){
				stacks.put(block0, block1);
			}
		}
	}

	private int calculate(State currentState){
		int count = 0;
		for(Predicate p: currentState){
			String name = p.getName();
			switch(name){
				case "on":
					String block0 = p.getParams().get(0);
					String block1 = p.getParams().get(1);
					if(!stacks.containsKey(block0) || stacks.get(block0) != block1){
						count++;
					}
					break;
			}
		}
		return count;
	}

}
