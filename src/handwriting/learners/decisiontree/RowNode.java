package handwriting.learners.decisiontree;

import handwriting.core.Drawing;

public class RowNode extends DTInner {
	private int row;
	private double minVal, maxVal;
	
	public RowNode(int row, DTSampleData data){
		this.row = row;
		setMinMax(data);
	}
	
	public RowNode(int row, DTSampleData data, boolean split){
		this.row = row;
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
		int rowBits = 0;
		
		for(int r = 0; r < d.getHeight(); r++){
			if(r == row){
				for(int c = 0; c < d.getWidth(); c++){
					if(d.isSet(r, c)){
						totalBits++;
						rowBits++;
					}
				}
			} else {
				for(int c = 0; c < d.getWidth(); c++){
					totalBits += d.isSet(r, c) ? 1 : 0;
				}
			}
		}
		
		return rowBits/(double)totalBits;
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
