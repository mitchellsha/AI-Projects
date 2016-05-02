package handwriting.learners.decisiontree;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.SampleData;
import search.core.Duple;

public class DTTrainer {
	private ArrayBlockingQueue<Double> progress;
	private DTSampleData baseData;
	private double currentProgress, tick;
	
	public DTTrainer(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		baseData = new DTSampleData(data);
		this.progress = progress;
		this.currentProgress = 0;
		progress.put(currentProgress);
		this.tick = 1.0 / data.numDrawings();
	}
	
	public DTNode train() throws InterruptedException {
		return train(baseData);
	}
	
	protected DTNode train(DTSampleData data) throws InterruptedException {
		if (data.numLabels() <= 1) {
			int num = data.numDrawings();
			DTLeaf leaf = new DTLeaf((num > 0) ? data.getLabelFor(0) : "Unknown");
			updateProgress(num);
			return leaf;
		} else {
			Duple<Integer, Integer> coord = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
			Duple<Integer, Integer> best = coord;
			double coordGini = getGiniForCoord(data, coord);
			double bestGini = coordGini;
			
			while(bestGini > (1 - 1/data.numLabels())){
				coord = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
				coordGini = getGiniForCoord(data, coord);
				if(coordGini < bestGini){
					best = coord;
					bestGini = coordGini;
				}
			}

			Duple<DTSampleData,DTSampleData> split = data.splitOn(best);
			return new PixelNode(coord, train(split.getFirst()), train(split.getSecond()));
		}
	}
	
	protected void updateProgress(int scale) throws InterruptedException{
		currentProgress += tick * scale;
		progress.put(currentProgress);
	}
	
	protected int randInt(int scale) {return (int) Math.floor(Math.random() * scale);}
	
	protected Duple<Integer, Integer> randCoord(int xScale, int yScale){
		int x = randInt(xScale);
		int y = randInt(yScale);
		return new Duple<>(x,y);
	}
	
	protected String randLabel(DTSampleData data){
		int i = randInt(data.numDrawings());
		return data.getLabelFor(i);
	}
	
	protected int getPixelCount(DTSampleData data, int x, int y){
		int count = 0;
		for(int i = 0; i < data.numDrawings(); i++){
			Drawing d = data.getDrawing(i);
			count += d.isSet(x, y) ? 1 : 0;
		}
		return count;
	}
	
	protected double getGiniForCoord(DTSampleData data, Duple<Integer, Integer> coord){
		Duple<DTSampleData,DTSampleData> split = data.splitOn(coord);
		double gini1 = split.getFirst().getGini();
		double gini2 = split.getSecond().getGini();
		return Math.sqrt(Math.pow(gini1, 2) + Math.pow(gini2, 2));
	}
}
