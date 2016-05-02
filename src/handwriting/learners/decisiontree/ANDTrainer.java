package handwriting.learners.decisiontree;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.SampleData;
import search.core.Duple;

public class ANDTrainer extends DTTrainer {
	
	public ANDTrainer(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		super(data, progress);
	}

	protected DTNode train(DTSampleData data) throws InterruptedException {
		if (data.numLabels() <= 1) {
			int num = data.numDrawings();
			DTLeaf leaf = new DTLeaf((num > 0) ? data.getLabelFor(0) : "Unknown");
			updateProgress(num);
			return leaf;
		} else {
			Duple<Integer, Integer> coord1 = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
			Duple<Integer, Integer> best1 = coord1;
			double coordGini = getGiniForCoord(data, coord1);
			double bestGini = coordGini;
			
			while(bestGini > (1 - 1/(double)data.numLabels())){
				coord1 = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
				coordGini = getGiniForCoord(data, coord1);
				if(coordGini < bestGini){
					best1 = coord1;
					bestGini = coordGini;
				}
			}

			Duple<Integer, Integer> coord2 = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
			Duple<Integer, Integer> best2 = coord2;
			coordGini = getGiniForCoords(data, best1, coord2);
			bestGini = coordGini;

			int limit = (data.getDrawingWidth() + data.getDrawingHeight()) / 2;
			while(bestGini > (1 - 1/data.numLabels()) && limit > 0){
				coord2 = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
				coordGini = getGiniForCoords(data, best1, coord2);
				if(coordGini < bestGini){
					best2 = coord2;
					bestGini = coordGini;
				} else limit--;
			}

			Duple<DTSampleData,DTSampleData> split1 = data.splitOn(best1);
			Duple<DTSampleData,DTSampleData> split2A = split1.getFirst().splitOn(best2);
			Duple<DTSampleData,DTSampleData> split2B = split1.getSecond().splitOn(best2);
			PixelNode nodeA = new PixelNode(best2, train(split2A.getFirst()), train(split2A.getSecond()));
			PixelNode nodeB = new PixelNode(best2, train(split2B.getFirst()), train(split2B.getSecond()));
			return new PixelNode(best1, nodeA, nodeB);
		}
	}
	
	protected double getGiniForCoords(DTSampleData data, Duple<Integer, Integer> coord1, Duple<Integer, Integer> coord2){
		Duple<DTSampleData,DTSampleData> split1 = data.splitOn(coord1);
		double gini1 = getGiniForCoord(split1.getFirst(), coord2);
		double gini2 = getGiniForCoord(split1.getSecond(), coord2);
		return Math.sqrt(Math.pow(gini1, 2) + Math.pow(gini2, 2));
	}
}
