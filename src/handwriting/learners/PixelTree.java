package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import handwriting.learners.decisiontree.DTNode;
import handwriting.learners.decisiontree.PixelTrainer;

public class PixelTree implements RecognizerAI {
	private DTNode root;

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		PixelTrainer trainer = new PixelTrainer(data, progress);
		root = trainer.train();
	}

	@Override
	public String classify(Drawing d) {
		return root.classify(d);
	}
}
