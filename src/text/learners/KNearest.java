package text.learners;

import java.util.ArrayList;
import java.util.Iterator;

import search.core.Duple;
import search.core.Histogram;
import text.core.Sentence;
import text.core.TextLearner;

public class KNearest implements TextLearner {
	private ArrayList<Duple<String,Sentence>> sentences = new ArrayList<>();
	private final int K = 3;

	@Override
	public void train(Sentence words, String lbl) {
		// Count up the relevant values
		sentences.add(new Duple<String,Sentence>(lbl, words));
	}

	@Override
	public String classify(Sentence words) {
		// Use the counted values for classification.
		ArrayList<Duple<String,Double>> bestDists = new ArrayList<Duple<String,Double>>();
		
		for(Duple<String,Sentence> dup: sentences){
			double dist = getDist(words, dup.getSecond());
			for(int i = 0; i < K; i++){
				if(i == bestDists.size() || dist < bestDists.get(i).getSecond()){
					bestDists.add(i, new Duple<String,Double>(dup.getFirst(), dist));
					break;
				}
			}
		}
		
		Histogram<String> votes = new Histogram<String>();
		for(int i = 0; i < bestDists.size() && i < K; i++){
			votes.bump(bestDists.get(i).getFirst());
		}
		
		return votes.getPluralityWinner();
	}
	
	private double getDist(Sentence wordsA, Sentence wordsB){
		return getDist(wordsA.wordCounts(), wordsB.wordCounts());
	}
	
	private double getDist(Histogram<String> wordsA, Histogram<String> wordsB){
		double dist = 0;
		
		Iterator<String> iter = wordsA.iterator();
		while(iter.hasNext()){
			String word = iter.next();
			dist += Math.abs(wordsA.getCountFor(word) - wordsB.getCountFor(word));
		}
		
		return dist;
	}
}
