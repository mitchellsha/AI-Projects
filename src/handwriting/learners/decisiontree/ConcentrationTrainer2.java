package handwriting.learners.decisiontree;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.SampleData;
import search.core.Duple;

public class ConcentrationTrainer2 extends ConcentrationTrainer {
	
	public ConcentrationTrainer2(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		super(data, progress);
	}

	protected DTNode train(DTSampleData data) throws InterruptedException {
		if(data.numLabels() <= 1) {
			int num = data.numDrawings();
			DTLeaf leaf = new DTLeaf((num > 0) ? data.getLabelFor(0) : "Unknown");
			updateProgress(num);
			return leaf;
		} else {
			Duple<Integer, Integer> coord = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
			ColumnNode colNode = new ColumnNode(coord.getFirst(), data, true);
			RowNode rowNode = new RowNode(coord.getSecond(), data, true);
			double colGini = getGiniForNode(data, colNode);
			double rowGini = getGiniForNode(data, rowNode);
			
			DTInner bestNode;
			double bestGini;
			if(colGini < rowGini){
				bestNode = colNode;
				bestGini = colGini;
			} else {
				bestNode = rowNode;
				bestGini = rowGini;
			}

			int limit = (int) Math.pow(data.getDrawingWidth() + data.getDrawingHeight(), 2);
			while(bestGini > 1 && limit > 0){
				coord = randCoord(data.getDrawingWidth(), data.getDrawingHeight());
				colNode = new ColumnNode(coord.getFirst(), data, true);
				rowNode = new RowNode(coord.getSecond(), data, true);
				colGini = getGiniForNode(data, colNode);
				rowGini = getGiniForNode(data, rowNode);
				
				if(colGini < bestGini || rowGini < bestGini){
					if(colGini < rowGini){
						bestNode = colNode;
						bestGini = colGini;
					} else {
						bestNode = rowNode;
						bestGini = rowGini;
					}
				} else limit--;
			}
			
			if(limit <= 0){
				/*
				System.out.print("Limit Reached... Labels: ");
				for(int i = 0; i < data.numDrawings(); i++){
					System.out.print(data.getLabelFor(i)+" ");
				}
				System.out.println();
				*/
				bestNode = getMostCommon(data);
			}
			Duple<DTSampleData,DTSampleData> split = data.splitOnNode(bestNode);
			bestNode.setChildren(train(split.getFirst()), train(split.getSecond()));
			return bestNode;
		}
	}
	
	private DTInner getMostCommon(DTSampleData data) throws InterruptedException {
		String bestLabel = "Unknown";
		for(String label: data.allLabels()){
			bestLabel = (data.numDrawingsFor(label) > data.numDrawingsFor(bestLabel)) ? label : bestLabel;
		}

		Duple<DTSampleData,DTSampleData> splitData1 = data.splitOnLabel(bestLabel);
		DTInner node1 = createNode(splitData1.getFirst());
		DTInner node2 = createNode(splitData1.getSecond());
		double gini1 = getGiniForNode(data, node1);
		double gini2 = getGiniForNode(data, node2);
		
		DTInner bestNode = (gini1 < gini2) ? node1 : node2;
		Duple<DTSampleData,DTSampleData> splitData2 = data.splitOnNode(bestNode);
		bestNode.setChildren(train(splitData2.getFirst()), train(splitData2.getSecond()));
		return bestNode;
	}
}
