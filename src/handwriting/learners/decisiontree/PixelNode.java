package handwriting.learners.decisiontree;

import handwriting.core.Drawing;
import search.core.Duple;

public class PixelNode extends DTInner {
	private int x, y;
	
	public PixelNode(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public PixelNode(Duple<Integer,Integer> coord){
		this.x = coord.getFirst();
		this.y = coord.getSecond();
	}
	
	public PixelNode(int x, int y, DTNode lc, DTNode rc){
		this.x = x;
		this.y = y;
		setChildren(lc, rc);
	}
	
	public PixelNode(Duple<Integer,Integer> coord, DTNode lc, DTNode rc){
		this.x = coord.getFirst();
		this.y = coord.getSecond();
		setChildren(lc, rc);
	}
	
	public boolean conditionMet(Drawing d){
		return d.isSet(x, y);
	}

}
