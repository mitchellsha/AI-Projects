package handwriting.learners;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.*;
import handwriting.learners.som.SOMPoint;
import handwriting.learners.som.SelfOrgMap;
import javafx.scene.canvas.Canvas;

@SuppressWarnings("restriction")
public class RecognizerSOM implements RecognizerAI {
	private SampleData data;
	private SelfOrgMap som;
	private HashMap<SOMPoint, String> table;
    private final int ITERATIONS = 10;
	
	public RecognizerSOM() {data = new SampleData();}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		this.data = data;
		int drawWidth = data.getDrawingWidth();
		int drawHeight = data.getDrawingHeight();
		int size = data.numLabels()*3;
		som = new SelfOrgMap(size, size, drawWidth, drawHeight);
		
		//Drawing[] drawings = altList();
		for(int i = 0; i < ITERATIONS; i++){
			progress.put(i / (double)ITERATIONS);
			double radius = ((ITERATIONS - i)/(ITERATIONS*1.0)) * (size/2.0);
			/*
			for(int d = 0; d < drawings.length; d++){
				som.train(drawings[d], radius);
				progress.put((i+(d / (double)drawings.length)) / (double)ITERATIONS);
			}
			*/
			///*
			for(int d = 0; d < data.numDrawings(); d++){
				som.train(data.getDrawing(d), radius);
				progress.put((i+(d / (double)data.numDrawings())) / (double)ITERATIONS);
			}
			//*/
		}
		initTable();
		progress.put(1.0);
	}

	@Override
	public String classify(Drawing d) {
		SOMPoint node = som.bestFor(d);
		return table.containsKey(node) ? table.get(node) : "Unknown";
	}

	@Override
	public void visualize(Canvas surface){
		som.visualize(surface);
	}
	
	private Drawing[] altList(){
		HashMap<String, Drawing[]> categorized = new HashMap<String, Drawing[]>();
		int count = 0;
		for(String label: data.allLabels()){
			count = data.numDrawingsFor(label);
			categorized.put(label, new Drawing[count]);
			Drawing[] temp =  categorized.get(label);
			for(int i = 0; i < count; i++){
				temp[i] = data.getDrawing(label, i);
			}
		}
		
		Drawing[] result = new Drawing[data.numDrawings()];
		int i = 0;
		for(int j = 0; j < count; j++){
			for(String label: categorized.keySet()){
				result[i] = categorized.get(label)[j];
				i++;
			}
		}
		return result;
	}
	
	private void initTable(){
		table = new HashMap<SOMPoint, String>();
		
		for(String label: data.allLabels()){
			for(int i = 0; i < data.numDrawingsFor(label); i++){
				SOMPoint node = som.bestFor(data.getDrawing(label, i));
				table.put(node, label);
			}
		}
	}
	
	public void printStats(double score){
		System.out.println(som.getStats()+"\t"+ITERATIONS+"\t"+score);
	}
}
