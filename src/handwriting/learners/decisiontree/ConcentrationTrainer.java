package handwriting.learners.decisiontree;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.SampleData;
import search.core.Duple;

public class ConcentrationTrainer extends DTTrainer {
	
	public ConcentrationTrainer(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		super(data, progress);
	}

	protected DTNode train(DTSampleData data) throws InterruptedException {
		if(data.numLabels() == 0){
			return new DTLeaf("Unknown");
		}
		if (data.numLabels() == 1) {
			DTLeaf leaf = new DTLeaf(data.getLabelFor(0));
			updateProgress(data.numDrawings());
			return leaf;
		} else {
			Duple<DTSampleData,DTSampleData> splitData1 = data.splitOnLabel(randLabel(data));
			DTInner node1 = createNode(splitData1.getFirst());
			DTInner node2 = createNode(splitData1.getSecond());
			double gini1 = getGiniForNode(data, node1);
			double gini2 = getGiniForNode(data, node2);
			DTInner node = (gini1 < gini2) ? node1 : node2;
			
			Duple<DTSampleData,DTSampleData> splitData2 = data.splitOnNode(node);
			DTNode leftChild = train(splitData2.getFirst());
			DTNode rightChild = train(splitData2.getSecond());
			node.setChildren(leftChild, rightChild);
			return node;
		}
	}
	
	protected DTInner createNode(DTSampleData data){
		int[] rowCounts = new int[data.getDrawingHeight()];
		int[] colCounts = new int[data.getDrawingWidth()];
		
		for(int r = 0; r < data.getDrawingHeight(); r++){
			for(int c = 0; c < data.getDrawingWidth(); c++){
				int count = getPixelCount(data, r, c);
				rowCounts[r] += count;
				colCounts[c] += count;
			}
		}
		
		int minRowIndx = 0,
			maxRowIndx = 0;
		for(int r = 0; r < data.getDrawingHeight(); r++){
			int count = rowCounts[r];
			if(count < rowCounts[minRowIndx]){
				minRowIndx = r;
			}else if(count > rowCounts[maxRowIndx]){
				maxRowIndx = r;
			}
		}
		
		int minColIndx = 0,
			maxColIndx = 0;
		for(int c = 0; c < data.getDrawingWidth(); c++){
			int count = colCounts[c];
			if(count < colCounts[minColIndx]){
				minColIndx = c;
			}else if(count > colCounts[maxColIndx]){
				maxColIndx = c;
			}
		}
		
		int colDiff = colCounts[maxColIndx] - colCounts[minColIndx],
			rowDiff = rowCounts[maxRowIndx] - rowCounts[minRowIndx];
		if(colDiff > rowDiff){
			int column = (colCounts[minColIndx] < data.getDrawingWidth() - colCounts[maxColIndx])
						? minColIndx
						: maxColIndx;
			
			return new ColumnNode(column, data);
		} else {
			int row = (rowCounts[minRowIndx] < data.getDrawingHeight() - rowCounts[maxRowIndx])
					? minRowIndx
					: maxRowIndx;
			
			return new RowNode(row, data);
		}
	}
	
	protected double getGiniForNode(DTSampleData data, DTInner node){
		Duple<DTSampleData,DTSampleData> split = data.splitOnNode(node);
		double gini1 = split.getFirst().getGini();
		double gini2 = split.getSecond().getGini();
		return Math.sqrt(Math.pow(gini1, 2) + Math.pow(gini2, 2));
	}
}
