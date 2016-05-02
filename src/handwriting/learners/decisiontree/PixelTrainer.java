package handwriting.learners.decisiontree;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.SampleData;
import search.core.Duple;
import search.core.Triple;

public class PixelTrainer extends DTTrainer {
	
	public PixelTrainer(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		super(data, progress);
	}

	protected DTNode train(DTSampleData data) throws InterruptedException {
		if(data.numLabels() == 0){
			return new DTLeaf("Unknown");
		}
		if (data.numLabels() == 1) {
			DTLeaf leaf = new DTLeaf(data.getLabelFor(0));
			updateProgress(data.numDrawings());
			return leaf;
		} else {
			Duple<Integer, Integer> coord = getDivisivePixel(data);
			Duple<DTSampleData,DTSampleData> split = data.splitOn(coord);
			return new PixelNode(coord, train(split.getFirst()), train(split.getSecond()));
		}
	}
	
	private Triple<Integer, Integer, Double> getDivisivePixel(DTSampleData data){
		String label = randLabel(data);
		return getMostCommonPixel(data.splitOnLabel(label).getFirst());
	}
	
	private Triple<Integer, Integer, Double> getMostCommonPixel(DTSampleData data){
		ArrayList<Triple<Integer, Integer, Double>> probs = new ArrayList<Triple<Integer, Integer, Double>>(data.numDrawings());
		
        for (int i = 0; i < data.getDrawingWidth(); ++i) {
            for (int j = 0; j < data.getDrawingHeight(); ++j) {
            	double p = getPixelCount(data, i, j) / data.numDrawings();
            	probs.add(new Triple<Integer, Integer, Double>(i, j, p));
            }
        }
        
        probs.sort((p1,  p2) -> p1.getThird().compareTo(p2.getThird()));
		return probs.get(probs.size()-1);
	}
}
