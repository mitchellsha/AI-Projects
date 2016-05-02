package maze.heuristics;

import maze.core.MazeCell;
import maze.core.MazeExplorer;
import search.core.BestFirstHeuristic;

public class ManhattanAreal implements BestFirstHeuristic<MazeExplorer> {

	public int getDistance(MazeExplorer node, MazeExplorer goal) {
		MazeCell loc1 = node.getLocation();
		MazeCell loc2 = goal.getLocation();
		int distX = Math.abs(loc1.X() - loc2.X());
		int distY = Math.abs(loc1.Y() - loc2.Y());
		int area = distX * distY;
		int manhattan = loc1.getManhattanDist(loc2);
		return (int) Math.ceil((area + manhattan)/2.0);
	}

}
