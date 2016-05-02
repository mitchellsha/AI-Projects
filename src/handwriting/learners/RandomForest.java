package handwriting.learners;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

import handwriting.core.*;

public class RandomForest implements RecognizerAI {
	private final int NUMBAGS = 10;
	private MultiBagger bagger;
	
	public RandomForest(){
		ArrayList<Supplier<RecognizerAI>> temp = new ArrayList<Supplier<RecognizerAI>>();
		temp.add(DecisionTree::new);
		temp.add(ANDTree::new);
		//temp.add(ConcentrationTree2::new);
		bagger = new MultiBagger(temp, NUMBAGS);
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
