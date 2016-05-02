package handwriting.learners;

import java.util.Arrays;

import handwriting.core.Drawing;

public class Transposer extends TrainedFitter {
	
	protected String fixDrawingString(Drawing draw){
		String result = draw.toString();
		int X = result.indexOf("X");
		int O = result.indexOf("O");
		result = (O < X || X == -1) ? result.substring(O) : result.substring(X);
		result = result.replace("X", "1");
		result = result.replace("O", "0");
		
		return transpose(result);
	}
	
	private String transpose(String drawString){
		String[] bitString = drawString.split("\\|");
		//Debug("Original: ", bitString);
		
		int top = 0;
		while(top < bitString.length && !bitString[top].contains("1")) top++;

		String[] bitString2 = new String[bitString.length];
		for(int i = 0; i < bitString.length; i++){
			bitString2[i] = bitString[(top+i) % bitString.length];
		}
		
		int left = bitString2[0].length();
		for(int i = 0; i < bitString.length - top; i++){
			int temp = bitString2[i].indexOf('1');
			left = (temp < left && temp != -1) ? temp : left;
		}

		String[] bitString3 = new String[bitString.length];
		for(int i = 0; i < bitString.length; i++){
			bitString3[i] = bitString2[i].substring(left) + bitString2[i].substring(0, left);
		}

		//Debug("Transposed: ", bitString3);
		String result = Arrays.toString(bitString3).replace(", ", "").replaceAll("[\\[\\]]", "");
		return result;
	}
    
	protected void Debug(String str, String[] values){
    	if(DEBUG){
    		System.out.println(str);
    		for(int i = 0; i < values.length; i++){
	    		System.out.println(values[i]);
    		}
    		System.out.println();
    	}
    }
}
