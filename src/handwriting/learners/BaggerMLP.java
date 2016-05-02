package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.*;

public class BaggerMLP implements RecognizerAI {
	private final int NUMBAGS = 30;
	private Bagger bagger;

	public BaggerMLP(){
		bagger = new Bagger(TrainedFitter::new, NUMBAGS);
	}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		bagger.train(data, progress);
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		return bagger.classify(d);
	}
}
