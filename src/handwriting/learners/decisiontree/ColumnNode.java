package handwriting.learners.decisiontree;

import handwriting.core.Drawing;

public class ColumnNode extends DTInner {
	private int column;
	private double minVal, maxVal;
	
	public ColumnNode(int col, DTSampleData data){
		column = col;
		setMinMax(data);
	}
	
	public ColumnNode(int col, DTSampleData data, boolean split){
		column = col;
		setMinMax(data);
		
		if(split){
			double mid = (maxVal + minVal)/2.0;
			maxVal = (maxVal + mid)/2.0;
			minVal = (minVal + mid)/2.0;
		}
	}

	@Override
	public boolean conditionMet(Drawing d) {
		double concen = concentration(d);
		return  (concen >= minVal) && (concen <= maxVal);
	}
	
	private double concentration(Drawing d){
		int totalBits = 0;
		int colBits = 0;

		for(int c = 0; c < d.getWidth(); c++){
			if(c == column){
				for(int r = 0; r < d.getHeight(); r++){
					if(d.isSet(r, c)){
						totalBits++;
						colBits++;
					}
				}
			} else {
				for(int r = 0; r < d.getHeight(); r++){
					totalBits += d.isSet(r, c) ? 1 : 0;
				}
			}
		}
		
		return colBits/(double)totalBits;
	}
	
	private void setMinMax(DTSampleData data){
		minVal = 1;
		maxVal = 0;
		
		for(int d = 0; d < data.numDrawings(); d++){
			double concen = concentration(data.getDrawing(d));
			minVal = (concen < minVal) ? concen : minVal;
			maxVal = (concen > maxVal) ? concen : maxVal;
		}
	}

}
