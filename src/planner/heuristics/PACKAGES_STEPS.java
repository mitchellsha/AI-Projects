package planner.heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planner.core.*;
import search.core.BestFirstHeuristic;

public class PACKAGES_STEPS implements BestFirstHeuristic<PlanStep> {

	private List<String> packages;
	private List<String> rooms;
	private HashMap<String, List<String>> connections;
	private HashMap<String, String> goalLocations;
	private HashMap<String, String> currLocations;
	private String robotLoc;
	private String inHand = "";
	private HashMap<String, HashMap<String, Integer>> roomDists;

	@Override
	public int getDistance(PlanStep node, PlanStep goal) {
		Domain dom = node.getDomain();
		//System.out.println("Domain: " + dom.getName());
		if(!dom.getName().equals("packages")){
			return 0;
		}
		
		if(packages == null && rooms == null){
			packages = new ArrayList<String>();
			rooms = new ArrayList<String>();
			connections = new HashMap<String, List<String>>();
			goalLocations = new HashMap<String, String>();
			currLocations = new HashMap<String, String>();
			roomDists = new HashMap<String, HashMap<String, Integer>>();
			setup(node.getProblem());
			//System.out.println(roomDists);
		}
		
		State currentState = node.getWorldState();
		updatePredicates(currentState);
		
		return calculate();
	}
	
	private void setup(Problem prob){
		State start = prob.getStartState();

		for(Predicate p: start){
			String name = p.getName();
			//System.out.println("Start Predicate: " + p);
			String temp1 = new String();
			String temp2 = new String();
			switch(name){
				case "room":
					temp1 = p.getParams().get(0);
					rooms.add(temp1);
					break;
				case "package":
					temp1 = p.getParams().get(0);
					packages.add(temp1);
					break;
				case "at":
					robotLoc = p.getParams().get(0);
					break;
				case "connected":
					temp1 = p.getParams().get(0);
					temp2 = p.getParams().get(1);
					addConnection(temp1, temp2);
					break;
				case "in":
					temp1 = p.getParams().get(0);
					temp2 = p.getParams().get(1);
					currLocations.put(temp1, temp2);
					break;
			}
		}

		State goal = prob.getGoals();
		
		for(Predicate p: goal){
			//System.out.println("Goal Predicate: " + p);
			String temp1 = p.getParams().get(0);
			String temp2 = p.getParams().get(1);
			goalLocations.put(temp1, temp2);
		}
	}
	
	private void addConnection(String room1, String room2){
		if(!connections.containsKey(room1)){
			connections.put(room1, new ArrayList<String>());
		}
		connections.get(room1).add(room2);
		
		if(!roomDists.containsKey(room1)){
			roomDists.put(room1, new HashMap<String, Integer>());
		}
		HashMap<String, Integer> hm = roomDists.get(room1);
		hm.put(room2, 1);
	}

	private void updatePredicates(State currentState){
		for(Predicate p: currentState){
			String name = p.getName();
			String temp1 = new String();
			String temp2 = new String();
			switch(name){
				case "at":
					robotLoc = p.getParams().get(0);
					break;
				case "in":
					temp1 = p.getParams().get(0);
					temp2 = p.getParams().get(1);
					currLocations.put(temp1, temp2);
					break;
				case "handempty":
					inHand = "";
					break;
				case "holding":
					temp1 = p.getParams().get(0);
					inHand = temp1;
					break;
			}
		}
	}
	
	private int calculate(){
		if(inHand == ""){
			String chosen = new String();
			int mini = rooms.size();
			for(String p: packages){
				String curr = currLocations.get(p);
				String goal = goalLocations.get(p);
				if(!curr.equals(goal)){
					int temp = distToRoom(curr);
					if(temp < mini){
						mini = temp;
						chosen = curr;
					}
				}
			}
			
			if(!chosen.equals("")){
				return mini;
			}
			return 0;
		} else {
			String goal = goalLocations.get(inHand);
			return distToRoom(goal);
		}
	}

	private int distToRoom(String room){
		return distToRoom(robotLoc, room);
	}

	private int distToRoom(String room1, String room2){
		if(!room1.equals(room2)){
			//System.out.print(room1 + ", " + room2 + "\t");
			if(roomDists.get(room1).containsKey(room2)){
				int val = roomDists.get(room1).get(room2);
				//System.out.println("Distance Known: " + val);
				return val;
			}
			int mini = rooms.size();
			for(String r: connections.get(room1)){
				if(!r.equals(robotLoc)){
					int temp = distToRoom(r, room2);
					mini = (temp < mini) ? temp : mini;
				} else {
					//System.out.println("LOOP");
					return mini;
				}
			}
			int val = mini + 1;
			roomDists.get(room1).put(room2, val);
			//System.out.println("Distance added: " + val);
			return val;
		}
		return 0;
	}
	
}
