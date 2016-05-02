package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import handwriting.learners.decisiontree.ConcentrationTrainer2;
import handwriting.learners.decisiontree.DTNode;

public class ConcentrationTree2 implements RecognizerAI {
	private DTNode root;

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		ConcentrationTrainer2 trainer = new ConcentrationTrainer2(data, progress);
		root = trainer.train();
	}

	@Override
	public String classify(Drawing d) {
		return root.classify(d);
	}
}
