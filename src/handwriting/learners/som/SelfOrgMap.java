package handwriting.learners.som;

import handwriting.core.Drawing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelfOrgMap {
	private int drawingWidth, drawingHeight;
	// Representation data type
	private int width, height;
    private double[][][][] weights;
    private int RADIUS;
	
	public SelfOrgMap(int width, int height, int dWidth, int dHeight) {
		this.drawingWidth = dWidth;
		this.drawingHeight = dHeight;

		this.width = width;
		this.height = height;
		RADIUS = (width + height);
        
        initArrays();
	}
	
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	public int getDrawingWidth() {return drawingWidth;}
	public int getDrawingHeight() {return drawingHeight;}
	
	public SOMPoint bestFor(Drawing example) {
		SOMPoint best = new SOMPoint(0, 0);
		double bestDist = drawingWidth * drawingHeight;
		
		for(int i = 0; i < getWidth(); i++){
			for(int j = 0; j < getHeight(); j++){
				SOMPoint node = new SOMPoint(i, j);
				double dist = distance(example, node);
				
				if(dist < bestDist){
					best = node;
					bestDist = dist;
				}
			}
		}
		
		return best;
	}
	
	public boolean isLegal(SOMPoint point) {
		return point.x() >= 0 && point.x() < getWidth() && point.y() >= 0 && point.y() < getHeight();
	}
	
	public void train(Drawing example) {
		SOMPoint best = bestFor(example);
		
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				SOMPoint node = new SOMPoint(x, y);
				addToWeights(node, example, rate(best.distanceTo(node)));
			}
		}
		
		RADIUS = (int) Math.ceil(RADIUS/2.0);
	}
	
	public void train(Drawing example, double radius) {
		RADIUS = (int) ((radius >= 1) ? radius : 1);
		
		train(example);
	}
	
	@SuppressWarnings("restriction")
	public Color getFillFor(int x, int y, SOMPoint node) {
		double val = weight(x, y, node);
		try{
			return Color.gray(1-val);
		} catch(IllegalArgumentException e){
			System.out.println("BAD VALUE: "+val+"\t("+x+", "+y+", "+node.toString()+")");
			return (val < 0) ? new Color(1, 0, 0, Math.abs(val)) : new Color(0, 0, 1, val);
		}
	}

	@SuppressWarnings("restriction")
	public void visualize(Canvas surface) {
		final double cellWidth = surface.getWidth() / getWidth();
		final double cellHeight = surface.getHeight() / getHeight();
		final double pixWidth = cellWidth / getDrawingWidth();
		final double pixHeight = cellHeight / getDrawingHeight();
		GraphicsContext g = surface.getGraphicsContext2D();
		g.clearRect(0, 0, surface.getWidth(), surface.getHeight());
		for (int x = 0; x < getWidth(); x++) { 
			for (int y = 0; y < getHeight(); y++) {
				SOMPoint cell = new SOMPoint(x, y);
				for (int x1 = 0; x1 < getDrawingWidth(); x1++) {
					for (int y1 = 0; y1 < getDrawingHeight(); y1++) {
						g.setFill(getFillFor(x1, y1, cell));
						g.fillRect(cellWidth * x + pixWidth * x1, cellHeight * y + pixHeight * y1, pixWidth, pixHeight);			
					}
				}
			}
		}
	}
	
	public double distance(Drawing example, SOMPoint node){
		double total = 0;
		double[][] ws = weights[node.x()][node.y()];
		
		for(int i = 0; i < drawingWidth; i++){
			for(int j = 0; j < drawingHeight; j++){
				double bit = example.isSet(i, j) ? 1 : 0;
				total += Math.pow(bit-ws[i][j], 2);
			}
		}
		
		return Math.sqrt(total);
	}
	
    private double weight(int i, int j, SOMPoint node) {return weights[node.x()][node.y()][i][j];}
    
    private void initArrays() {
        weights = new double[width][height][drawingWidth][drawingHeight];
        
        for(int w = 0; w < width; w++){
            for(int h = 0; h < height; h++){
            	for(int dw = 0; dw < drawingWidth; dw++){
                	for(int dh = 0; dh < drawingHeight; dh++){
                    	weights[w][h][dw][dh] = Math.random();
                    	//weights[w][h][dw][dh] = 0.5;
                	}
            	}
            }
        }
    }

    private void addToWeights(SOMPoint node, Drawing example, double rate) {
		double[][] ws = weights[node.x()][node.y()];
		
        for (int i = 0; i < drawingWidth; ++i) {
            for (int j = 0; j < drawingHeight; ++j) {
				double bit = example.isSet(i, j) ? 1 : 0;
				double val = bit - ws[i][j];
				ws[i][j] += val*rate;
            }
        }
    }
    
    private double rate(double d){
    	double e = Math.E;
    	double numer = -Math.pow(d, 2);
    	double power = numer/(2*Math.pow(RADIUS, 2));
    	return Math.pow(e, power);
    }
    
    public String getStats(){
    	int size = getWidth() * getHeight();
    	return size+"\t"+RADIUS;
    }
}
