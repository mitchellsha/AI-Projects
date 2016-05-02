package maze.heuristics;

import maze.core.MazeCell;
import maze.core.MazeExplorer;
import search.core.BestFirstHeuristic;

public class Euclidean implements BestFirstHeuristic<MazeExplorer> {

	public int getDistance(MazeExplorer node, MazeExplorer goal) {
		MazeCell loc1 = node.getLocation();
		MazeCell loc2 = goal.getLocation();
		int a = loc1.X() - loc2.X();
		int b = loc1.Y() - loc2.Y();
		double c = Math.sqrt(a^2 + b^2);
		return (int) c;
	}

}
