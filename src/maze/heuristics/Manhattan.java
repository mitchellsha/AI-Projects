package maze.heuristics;

import maze.core.MazeCell;
import maze.core.MazeExplorer;
import search.core.BestFirstHeuristic;

public class Manhattan implements BestFirstHeuristic<MazeExplorer> {

	public int getDistance(MazeExplorer node, MazeExplorer goal) {
		MazeCell loc1 = node.getLocation();
		MazeCell loc2 = goal.getLocation();
		return loc1.getManhattanDist(loc2);
	}

}
