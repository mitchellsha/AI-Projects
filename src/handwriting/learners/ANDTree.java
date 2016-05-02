package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import handwriting.learners.decisiontree.ANDTrainer;
import handwriting.learners.decisiontree.DTNode;

public class ANDTree implements RecognizerAI {
	private DTNode root;

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		ANDTrainer trainer = new ANDTrainer(data, progress);
		root = trainer.train();
	}

	@Override
	public String classify(Drawing d) {
		return root.classify(d);
	}
}
