package maze.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import search.core.BestFirstObject;

public class MazeExplorer implements BestFirstObject<MazeExplorer> {
	private Maze m;
	private MazeCell location;
	private TreeSet<MazeCell> treasureFound; 
	
	public MazeExplorer(Maze m, MazeCell location) {
		this.m = m;
		this.location = location;
		treasureFound = new TreeSet<MazeCell>();
	}
	
	public MazeCell getLocation() {return location;}

	@Override
	public ArrayList<MazeExplorer> getSuccessors() {
		ArrayList<MazeExplorer> result = new ArrayList<MazeExplorer>();
		// TODO: It should add as a successor every adjacent, unblocked neighbor square.
		for(MazeCell mc: m.getNeighbors(location)){
			if(!m.blocked(location, mc)){
				MazeExplorer me = new MazeExplorer(m, mc);
				TreeSet<MazeCell> ts = new TreeSet<MazeCell>(treasureFound);
				if(m.isTreasure(mc)){
					ts.add(mc);
				}
				me.addTreasures(ts);
				result.add(me);
			}
		}
        return result;
	}
	
	public void addTreasures(Collection<MazeCell> treasures) {
		treasureFound.addAll(treasures);
	}
	
	public String toString() {
		StringBuilder treasures = new StringBuilder();
		for (MazeCell t: treasureFound) {
			treasures.append(";");
			treasures.append(t.toString());
		}
		return "@" + location.toString() + treasures.toString();
	}
	
	@Override
	public int hashCode() {return toString().hashCode();}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof MazeExplorer) {
			return achieves((MazeExplorer)other);
		} else {
			return false;
		}
	}

	@Override
	public boolean achieves(MazeExplorer goal) {
		return this.location.equals(goal.location) && this.treasureFound.equals(goal.treasureFound);
	}

}
