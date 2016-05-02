package handwriting.learners;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import search.core.Duple;

public class TrainedFitter implements RecognizerAI {
	protected SampleData data;
	private MultiLayer ml;
	private int numIn;
	private int numHid;
	private int numOut;
	private int iterations;
	private double rate = 0.375;
	private double[][] inputs;
	private double[][] targets;
	private HashMap<String, double[]> labels;
    
	protected final boolean DEBUG = false;
	
	public TrainedFitter() {data = new SampleData();}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		progress.put(0.0);
		this.data = data;
		
		numIn = data.getDrawingHeight() * data.getDrawingWidth();
		numOut = bitsNeeded(data.numLabels());
		numHid = (int) (log2(numIn) + numOut);
		iterations = numIn * numOut;
		
		ml = new MultiLayer(numIn, numHid, numOut);
		prepare();
		//progress.put(0.1);
		//Debug("INPUT:", inputs);
		//Debug("TARGETS:", targets);

		ml.trainN(inputs, targets, iterations, rate, progress);
		progress.put(1.0);
		
		printStats();
	}

	@Override
	public String classify(Drawing d) {
		if(ml == null) return "Unknown";
		
		double[] output = ml.compute(convertDrawingToBits(d));
		//Debug("OUTPUT: ", output);
		return decode(output);
	}
	
	private int bitsNeeded(int base10){
		return (int) Math.ceil(log2(base10))+1;
	}
	
	private double log2(int i){
		return Math.log(i) / Math.log(2);
	}
	
	private double[] toBinary(int base10, int length){
		int b10 = base10;
		double[] bits = new double[length];
		for(int i = length-1; i >= 0; i--){
			bits[i] = b10 % 2;
			b10 /= 2;
		}
		//Debug(base10+" -> ", bits);
		return bits;
	}
	
	private void prepare(){
		labels = new HashMap<String, double[]>();
		
		for(int l = 0; l < data.numLabels(); l++){
			String name = (String) data.allLabels().toArray()[l];
			double[] val = toBinary(l, numOut);
			labels.put(name, val);
			//Debug("Label: "+name+"\tValue: ", val);
		}
		
		
		inputs = new double[data.numDrawings()][numIn];
		targets = new double[data.numDrawings()][numOut];

		for(int d = 0; d < data.numDrawings(); d++){
			Duple<String,Drawing> temp = data.getLabelAndDrawing(d);
			targets[d] = labels.get(temp.getFirst());
			inputs[d] = convertDrawingToBits(temp.getSecond());
		}
	}
	
	private String decode(double[] output){
		for(String l: labels.keySet()){
			if(equalData(labels.get(l), output)) return l;
		}
		return "Unknown";
	}
	
	private double[] convertDrawingToBits(Drawing draw){
		String temp = fixDrawingString(draw);
		
		double[] result = new double[temp.length()];
		for(int i = 0; i < temp.length(); i++){
			result[i] = Double.parseDouble(temp.substring(i,i+1));
		}
		
		return result;
	}
	
	protected String fixDrawingString(Drawing draw){
		String result = draw.toString();
		int X = result.indexOf("X");
		int O = result.indexOf("O");
		result = (O < X || X == -1) ? result.substring(O) : result.substring(X);
		result = result.replace("|", "");
		result = result.replace("X", "1");
		result = result.replace("O", "0");
		
		return result;
	}
    
	protected void Debug(String str){
    	if(DEBUG) System.out.println(str);
    }
    
	protected void Debug(String str, double[][] values){
    	if(DEBUG){
    		System.out.println(str);
    		for(int i = 0; i < values.length; i++){
    			Debug("", values[i]);
    		}
    	}
    }
    
	protected void Debug(String str, double[] values){
    	if(DEBUG){
    		System.out.print(str);
    		for(int i = 0; i < values.length; i++){
	    		System.out.print(values[i]+" ");
    		}
    		System.out.println();
    	}
    }
    
    private boolean equalData(double[] a, double[] b){
    	if(a.length != b.length) return false;
    	for(int i = 0; i < a.length; i++){
        	if((int)a[i] != (int)Math.round(b[i])) return false;
    	}
    	return true;
    }
    
    private void printStats(){
    	if(DEBUG){
        	String name = this.getClass().toString();
        	int i = name.lastIndexOf(".");
        	name = name.substring(i+1);
        	System.out.println("RECOGNIZER: "+name);
        	System.out.println("IN: "+numIn+"\tOUT: "+numOut+"\t\tHID: "+numHid);
        	System.out.println("ITERATIONS: "+iterations+"\t\tRATE: "+rate);
    	}
    }
}
