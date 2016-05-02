package text.core;

import search.core.Histogram;

public class TestResult {
	private Histogram<String> correctForLabel;
	private Histogram<String> numberForLabel;
	
	public TestResult(TextLearner learner, LabeledWords testSet) {
		correctForLabel = learner.test(testSet);
		numberForLabel = testSet.allCounts();
		
		System.out.println("TestResult: TotalCounts: " + numberForLabel.getTotalCounts() + " testSet: " + testSet.size());
	}
	
	public Iterable<String> allLabels() {return numberForLabel;}
	
	public int getCorrectFor(String lbl) {
		return correctForLabel.getCountFor(lbl);
	}
	
	public int getTotalFor(String lbl) {
		return numberForLabel.getCountFor(lbl);
	}
	
	public double getRatioFor(String lbl) {
		return (double)getCorrectFor(lbl) / getTotalFor(lbl);
	}
	
	public int getCorrect() {
		return correctForLabel.getTotalCounts();
	}
	
	public int getTotal() {
		return numberForLabel.getTotalCounts();
	}
	
	public double getRatio() {
		return (double)getCorrect() / getTotal();
	}
}
