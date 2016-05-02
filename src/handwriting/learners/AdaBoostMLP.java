package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;

public class AdaBoostMLP implements RecognizerAI {
	private final int NUMBAGS = 2;
	private AdaBooster adaboost;
	
	public AdaBoostMLP(){
		adaboost = new AdaBooster(TrainedFitter::new, NUMBAGS);
	}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		adaboost.train(data, progress);
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		return adaboost.classify(d);
	}
	
}
