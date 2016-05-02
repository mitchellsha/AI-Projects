package text.learners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import search.core.Duple;
import search.core.Histogram;
import text.core.Sentence;
import text.core.TextLearner;

public class NaiveBayes implements TextLearner {
	private HashMap<String,Histogram<String>> labelCounts = new HashMap<String,Histogram<String>>();
	private ArrayList<String> vocab;

	@Override
	public void train(Sentence words, String lbl) {
		// Count up the relevant values
		if(!labelCounts.containsKey(lbl)){
			labelCounts.put(lbl, new Histogram<String>(words.wordCounts()));
		} else {
			labelCounts.put(lbl, combine(labelCounts.get(lbl), words.wordCounts()));
		}
	}
	
	private Histogram<String> combine(Histogram<String> histA, Histogram<String> histB){
		Iterator<String> iter = histB.iterator();
		while(iter.hasNext()){
			String label = iter.next();
			histA.bumpBy(label, histB.getCountFor(label));
		}
		return histA;
	}

	@Override
	public String classify(Sentence words) {
		// Use the counted values for classification.
		Duple<String,Double> best = new Duple<String,Double>("Unknown", 0.0);
		for(String label: labelCounts.keySet()){
			double prob = getProb(words, label);
			best = (prob > best.getSecond()) ? new Duple<String,Double>(label, prob) : best;
		}
		return best.getFirst();
	}
	
	private double getProb(Sentence words, String label){
		double prob = 1;
		Iterator<String> iter = words.iterator();
		while(iter.hasNext()){
			String word = iter.next();
			prob *= getProb(word, label);
		}
		return prob;
	}
	
	private double getProb(String word, String label){
		Histogram<String> counts = labelCounts.get(label);
		return (counts.getCountFor(word) + 1) / (double)(counts.getTotalCounts() + getVocabCount());
	}
	
	private int getVocabCount(){
		if(vocab == null){
			initializeVocab();
		}
		return vocab.size();
	}
	
	private void initializeVocab(){
		vocab = new ArrayList<String>();
		for(String label: labelCounts.keySet()){
			Histogram<String> counts = labelCounts.get(label);
			Iterator<String> iter = counts.iterator();
			while(iter.hasNext()){
				String word = iter.next();
				if(!vocab.contains(word)){
					vocab.add(word);
				}
			}
		}
	}
}
