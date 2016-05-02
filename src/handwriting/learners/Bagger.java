package handwriting.learners;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import search.core.Duple;
import search.core.Histogram;

public class Bagger implements RecognizerAI {
	private ArrayList<RecognizerAI> bags;
	private Supplier<RecognizerAI> supplier;
	private int numBags;
	
	// For the "supplier" parameter, use the constructor; for example, 
	// b = new Bagger(DecisionTree::new, 30)
	public Bagger(Supplier<RecognizerAI> supplier, int numBags) {
		this.numBags = numBags;
		this.supplier = supplier;
		this.bags = new ArrayList<>();
	}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		// TODO: Reset "bags" to be empty.  Then create "numBags" instances 
		// of whatever learner is being bagged.  For each of these instances,
		// recreate the training data by random sampling with replacement. 
		// Then train that instance using the rebuilt data.
		progress.put(0.0);
		
		bags = new ArrayList<>();
		System.out.print("Training bag ");
		for(int i = 0; i < numBags; i++){
			System.out.print(i+" ");
			bags.add(supplier.get());
			
			SampleData testData = randSample(data);
			progress.put((i*1.0)/numBags);
			bags.get(i).train(testData, new ArrayBlockingQueue<Double>(data.numDrawings()));
		}
		System.out.println();
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		// TODO: Use a Histogram (from search.core) to count the labels
		// returned by calling "classify(d)" on each learner.  Then
		// return the plurality winner.
		
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
