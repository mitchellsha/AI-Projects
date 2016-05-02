package handwriting.learners;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import search.core.Duple;
import search.core.Histogram;

public class MultiBagger implements RecognizerAI {
	private ArrayList<RecognizerAI> bags;
	private ArrayList<Supplier<RecognizerAI>> suppliers;
	private int numBags;
	
	public MultiBagger(ArrayList<Supplier<RecognizerAI>> suppliers, int numBags) {
		this.numBags = numBags;
		this.suppliers = suppliers;
		this.bags = new ArrayList<>();
	}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		progress.put(0.0);
		
		bags = new ArrayList<>();
		System.out.print("Training bag ");
		for(int i = 0; i < numBags; i++){
			for(Supplier<RecognizerAI> supplier: suppliers){
				bags.add(supplier.get());
			}
		}
		for(int i = 0; i < bags.size(); i++){
			System.out.print(i+" ");
			
			SampleData testData = randSample(data);
			progress.put((i*1.0)/bags.size());
			bags.get(i).train(testData, new ArrayBlockingQueue<Double>(data.numDrawings()));
		}
		System.out.println();
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		Histogram<String> histogram = new Histogram<String>();
		for(RecognizerAI rai: bags){
			String result = rai.classify(d);
			histogram.bump(result);
		}
		
		return histogram.getPluralityWinner();
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

}
