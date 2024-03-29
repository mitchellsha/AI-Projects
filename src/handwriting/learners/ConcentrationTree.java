package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import handwriting.learners.decisiontree.ConcentrationTrainer;
import handwriting.learners.decisiontree.DTNode;

public class ConcentrationTree implements RecognizerAI {
	private DTNode root;

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		ConcentrationTrainer trainer = new ConcentrationTrainer(data, progress);
		root = trainer.train();
	}

	@Override
	public String classify(Drawing d) {
		return root.classify(d);
	}
}
