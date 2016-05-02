package handwriting.learners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import search.core.Duple;
import search.core.Histogram;

public class AdaBooster implements RecognizerAI {
	private ArrayList<RecognizerAI> bags;
	private Supplier<RecognizerAI> supplier;
	private int numBags;
	private SampleData baseData;
	private HashMap<String, Double> distribution;
	private double minDist;
	
	public AdaBooster(Supplier<RecognizerAI> supplier, int numBags) {
		this.numBags = numBags;
		this.supplier = supplier;
		this.bags = new ArrayList<>();
	}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		progress.put(0.0);
		baseData = data;
		initBags(progress);
		initDist();
		
		double rounds = baseData.numDrawings();
		//double rounds = 1;
		for(int i = 0; i < rounds; i++){
			progress.put(i/rounds);
			HashMap<String, Boolean> hypothesis = new HashMap<String, Boolean>(baseData.numLabels());
			double error = 0;
			double normConst = 0;
			for(String label: baseData.allLabels()){
				int num = baseData.numDrawingsFor(label);
				int d = (i < num) ? i : randInt(num);
				Drawing draw = baseData.getDrawing(label, d);
				String tempLabel = classify(draw);
				boolean temp = (tempLabel.equals(label)) ? true : false;
				hypothesis.put(label, temp);
				error += (!hypothesis.get(label)) ? distribution.get(label) : 0;
				normConst = (distribution.get(label) > normConst) ? distribution.get(label) : normConst;
			}
			error = (error > 0.5) ? 0.5 : error;
			
			double beta = error / (1.0-error);
			minDist = normConst;
			if(beta != 0){
				for(String label: baseData.allLabels()){
					distribution.put(label, distribution.get(label) / normConst * (hypothesis.get(label) ? beta : 1.0));
					minDist = (distribution.get(label) < minDist) ? distribution.get(label) : minDist;
				}
			}
			
		}
		
		for(String key: distribution.keySet()){
			System.out.println(key+": "+distribution.get(key));
		}
		System.out.println();
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		Histogram<String> histogram = new Histogram<String>();
		for(RecognizerAI rai: bags){
			String result = rai.classify(d);
			int val = (int) Math.ceil(distribution.getOrDefault(result, 0.0)/minDist);
			histogram.bumpBy(result, val);
		}
		
		String result = histogram.getPluralityWinner();
		return (result == null) ? "Unknown" : result;
	}
	
	private SampleData randSample(SampleData data){
		SampleData newData = new SampleData();
		
		for(int i = 0; i < data.numDrawings(); i++){
			int j = randInt(data.numDrawings());
			Duple<String, Drawing> dup = data.getLabelAndDrawing(j);
			newData.addDrawing(dup.getFirst(), dup.getSecond());
		}
		
		return newData;
	}
	
	private int randInt(int scale) {return (int) Math.floor(Math.random() * scale);}
	
	private void initBags(ArrayBlockingQueue<Double> progress) throws InterruptedException{
		bags = new ArrayList<>();
		for(int i = 0; i < numBags; i++){
			bags.add(supplier.get());
			
			SampleData testData = randSample(baseData);
			bags.get(i).train(testData, progress);
		}
	}
	
	private void initDist(){
		distribution = new HashMap<String, Double>(baseData.numLabels());
		for(String label: baseData.allLabels()){
			distribution.put(label, 1.0/baseData.numLabels());
		}
		minDist = 1.0/baseData.numLabels();
	}

}
