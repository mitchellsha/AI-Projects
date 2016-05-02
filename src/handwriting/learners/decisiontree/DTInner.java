package handwriting.learners.decisiontree;

import handwriting.core.Drawing;

public abstract class DTInner implements DTNode {
	protected DTNode leftChild, rightChild;
	
	public String classify(Drawing d){
		return conditionMet(d) ? leftChild.classify(d) : rightChild.classify(d);
	}
	
	public abstract boolean conditionMet(Drawing d);
	
	public void setChildren(DTNode lc, DTNode rc){
		leftChild = lc;
		rightChild = rc;
	}
	
	public void setLeftChild(DTNode lc)  {leftChild = lc;}
	public void setRightChild(DTNode rc) {rightChild = rc;}
}
