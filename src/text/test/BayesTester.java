package text.test;

import java.io.FileNotFoundException;

import text.learners.KNearest;
import text.learners.NaiveBayes;

public class BayesTester {
	public static void test(String[] args, DataSetReader dataReader) throws FileNotFoundException {
		SimpleTester.test(args, dataReader, (train, test) -> SimpleTester.conductTest("Naive Bayes", new NaiveBayes(), train, test));
		SimpleTester.test(args, dataReader, (train, test) -> SimpleTester.conductTest("K-Nearest Neighbors", new KNearest(), train, test));
	}
}
