package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;

public class AdaBoostSOM implements RecognizerAI {
	private final int NUMBAGS = 3;
	private AdaBooster adaboost;
	
	public AdaBoostSOM(){
		adaboost = new AdaBooster(RecognizerSOM::new, NUMBAGS);
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
