package handwriting.learners;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.*;

public class BestFitter implements RecognizerAI {
	private SampleData data;
	
	public BestFitter() {data = new SampleData();}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		this.data = data;
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		String bestLabel = "Unknown";
		int bestCount = (int) (d.getHeight() * d.getWidth() * 0.7);
		
		for (String label: data.allLabels()) {
			for (int i = 0; i < data.numDrawingsFor(label); ++i) {
				Drawing other = data.getDrawing(label, i);
				if (d.equals(other)) {
					return label;
				} else {
					int count = matchingBits(d, other);
					if(count > bestCount){
						bestCount = count;
						bestLabel = new String(label);
					}
				}
			}
		}
		
		return bestLabel;
	}
	
	private int matchingBits(Drawing a, Drawing b){
		int count = 0;
		
		for(int x = 0; x < a.getWidth(); x++){
			for(int y = 0; y < a.getHeight(); y++){
				count += (a.isSet(x, y) == b.isSet(x, y)) ? 1 : 0;
			}
		}
		
		return count;
	}
}
