package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;

public class BaggerSOM implements RecognizerAI {
	private final int NUMBAGS = 5;
	private Bagger bagger;
	
	public BaggerSOM(){
		bagger = new Bagger(RecognizerSOM::new, NUMBAGS);
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
