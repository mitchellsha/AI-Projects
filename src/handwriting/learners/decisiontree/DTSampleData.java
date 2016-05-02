package handwriting.learners.decisiontree;

import handwriting.core.Drawing;
import handwriting.core.SampleData;
import search.core.Duple;

public class DTSampleData extends SampleData {
	public DTSampleData() {super();}
	
	public DTSampleData(SampleData src) {
		for (int i = 0; i < src.numDrawings(); i++) {
			this.addDrawing(src.getLabelFor(i), src.getDrawing(i));
		}
	}
	
	public double getGini() {
		// TODO: Implement Gini coefficient for this set
		double total = 0;
		for(String label: allLabels()){
			double Pi = numDrawingsFor(label) / (double)numDrawings();
			total += Math.pow(Pi, 2);
		}
		return 1.0 - total;
	}
	
	public Duple<DTSampleData,DTSampleData> splitOn(int x, int y) {
		DTSampleData on = new DTSampleData();
		DTSampleData off = new DTSampleData();
		
		// TODO: Add all elements with (x, y) set to "on"
		//       Add all elements with (x, y) not set to "off"
		for(String label: allLabels()){
			for(int i = 0; i < numDrawingsFor(label); i++){
				Drawing d = getDrawing(label, i);
				if(d.isSet(x, y)) {
					on.addDrawing(label, d);
				} else {
					off.addDrawing(label, d);
				}
			}
		}
		
		return new Duple<>(on, off);
	}
	
	public Duple<DTSampleData,DTSampleData> splitOn(Duple<Integer,Integer> coord) {
		return splitOn(coord.getFirst(), coord.getSecond());
	}
	
	public Duple<DTSampleData,DTSampleData> splitOnLabel(String label) {
		DTSampleData on = new DTSampleData();
		DTSampleData off = new DTSampleData();

		for(int i = 0; i < numDrawings(); i++){
			Duple<String,Drawing> dup = this.getLabelAndDrawing(i);
			if(label.equals(dup.getFirst())) {
				on.addDrawing(dup);
			} else {
				off.addDrawing(dup);
			}
		}
		
		return new Duple<>(on, off);
	}
	
	public Duple<DTSampleData,DTSampleData> splitOnNode(DTInner node) {
		DTSampleData on = new DTSampleData();
		DTSampleData off = new DTSampleData();
		
		for(int i = 0; i < numDrawings(); i++){
			Duple<String,Drawing> dup = getLabelAndDrawing(i);
			if(node.conditionMet(dup.getSecond())) {
				on.addDrawing(dup);
			} else {
				off.addDrawing(dup);
			}
		}
		
		return new Duple<>(on, off);
	}
	
	private void addDrawing(Duple<String,Drawing> info){
		addDrawing(info.getFirst(), info.getSecond());
	}
}
