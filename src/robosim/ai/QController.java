package robosim.ai;

import java.util.HashMap;

import robosim.core.Action;
import robosim.core.Simulator;
import search.core.Duple;

public class QController implements Controller {
	private HashMap<String, Double> Q = new HashMap<>();
	private int timestep = 0;
	private double
		gamma = 0.5,
		alpha = 1;
	private final int LAYERWIDTH = 15;
	
	@Override
	public void control(Simulator sim) {
		Duple<String, Double> before = getStateAndScore(sim);
		Action action = recommendAction(before.getFirst());
		action.applyTo(sim);
		sim.move();

		Duple<String, Double> after = getStateAndScore(sim);
		updateQ(before, action, after);
		updateAlpha();

		recommendAction(after.getFirst()).applyTo(sim);
	}
	
	private Duple<String, Double> getStateAndScore(Simulator sim){
		String state = getState(sim);
		double score = getScore(sim);
		return new Duple<>(state, score);
	}
	
	private String getState(Simulator sim){
		if(sim.isColliding()) {
			return "COLLIDING";
		}
		
		if(sim.findClosest() < LAYERWIDTH) {
			return "CLOSE";
		}

		int count = (int) Math.floor((sim.findClosest() - LAYERWIDTH) / LAYERWIDTH);
		String result = "SAFE";
		for(int i = 0; i < count; i++){
			result += "ER";
		}
		
		return result;
	}
	
	private double getScore(Simulator sim){
		int fwds = sim.getForwardMoves();
		int collisions = sim.getCollisions();
		int moves = sim.getTotalMoves();
		return Math.pow(fwds, 3)/(Math.pow(moves, 2)+1) - Math.pow(collisions, 2);
	}
	
	private Action recommendAction(String state){
		Duple<Action, Double> best = null;
		
		for(Action action: Action.values()){
			double reward = getReward(state, action);
			if(reward > alpha && (best == null || best.getSecond() < reward)){
				best = new Duple<>(action, reward);
			}
		}
		
		return best != null ? best.getFirst() : randAction();
	}
	
	private Action randAction(){
		Action[] actions = Action.values();
		int i = (int) Math.floor(Math.random() * actions.length);
		return actions[i];
	}
	
	private double getReward(String state, Action action)  {return getReward(state+":"+action.toString());}
	private double getReward(String sa) {return Q.containsKey(sa) ? Q.get(sa) : 0;}
	
	private void updateQ(Duple<String, Double> before, Action action, Duple<String, Double> after){
		Action aPrime = recommendAction(after.getFirst());
		double rPrime = getReward(after.getFirst(), aPrime);
		double r = after.getSecond() - before.getSecond();
		
		double val = (1-alpha) * getReward(before.getFirst(), action) + alpha * (gamma * rPrime + r);
		Q.put(before.getFirst()+":"+action, val);
	}
	
	private void updateAlpha(){
		double c = 100;
		alpha = 1/(1 + (timestep/c));
		timestep++;
	}
}
