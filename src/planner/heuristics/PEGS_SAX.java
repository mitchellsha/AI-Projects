package planner.heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planner.core.*;
import search.core.BestFirstHeuristic;

public class PEGS_SAX implements BestFirstHeuristic<PlanStep> {

	private HashMap<String, HashMap<String, String>> jumps;
	private HashMap<String, HashMap<String, String>> jumpedBy;
	private List<String> holes;

	@Override
	public int getDistance(PlanStep node, PlanStep goal) {
		Domain dom = node.getDomain();
		//System.out.println("Domain: " + dom.getName());
		if(!dom.getName().equals("pegs")){
			return 0;
		}
		
		if(holes == null){
			jumps = new HashMap<String, HashMap<String, String>>();
			jumpedBy = new HashMap<String, HashMap<String, String>>();
			setup(node.getProblem());
			//System.out.println(jumps);
			//System.out.println(jumpedBy);
		}
		int val = 2 - SAX(node.getWorldState());
		//System.out.println("SAX: " + val);
		return val;
	}
	
	private void setup(Problem prob){
		State start = prob.getStartState();

		for(Predicate p: start){
			String name = p.getName();
			//System.out.println("Start Predicate: " + p + "\tNum Params: " + p.numParams());
			switch(name){
				case "jump":
					String temp0 = p.getParams().get(0);
					String temp1 = p.getParams().get(1);
					String temp2 = p.getParams().get(2);
					addJump(temp0, temp1, temp2);
					break;
			}
		}
		
		holes = new ArrayList<String>(jumps.keySet());
		for(String key: jumpedBy.keySet()){
			if(!holes.contains(key)){
				holes.add(key);
			}
		}
	}
	
	private void addJump(String peg1, String peg2, String peg3){
		if(!jumps.containsKey(peg1)){
			jumps.put(peg1, new HashMap<String, String>());
		}
		jumps.get(peg1).put(peg2, peg3);

		if(!jumps.containsKey(peg3)){
			jumps.put(peg3, new HashMap<String, String>());
		}
		jumps.get(peg3).put(peg2, peg1);

		if(!jumpedBy.containsKey(peg2)){
			jumpedBy.put(peg2, new HashMap<String, String>());
		}
		jumpedBy.get(peg2).put(peg1, peg3);
		jumpedBy.get(peg2).put(peg3, peg1);
	}

	private int SAX(State currentState){
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
		
		int total = 0;
		for(String h: holes){
			total += empties.contains(h) ? 0 : getPegScore(h);
		}
		return total;
	}
	
	private int getPegScore(String peg){
		int a,x;
		
		a = jumpedBy.containsKey(peg) ? (int)Math.ceil(jumpedBy.get(peg).size()/2.0) : 0;
		x = jumps.containsKey(peg) ? (int)Math.ceil(jumps.get(peg).size()/2.0) : 0;
		
		int result = a - x;
		//result = (Math.abs(result) > 1) ? (result / Math.abs(result)) : result;
		
		//System.out.println(peg + ": " + result + " (" + a + " - " + x + ")");
		return result;
	}

}
