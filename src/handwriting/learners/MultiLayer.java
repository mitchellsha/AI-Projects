package handwriting.learners;

public class MultiLayer extends PerceptronNet {
    private Perceptron inputToHidden, hiddenToOutput;
    
    // Invariant: 
    //   inputToHidden.numOutputNodes() == hiddenToOutput.numInputNodes()
    
    public int numInputNodes() {return inputToHidden.numInputNodes();}
    public int numHiddenNodes() {return inputToHidden.numOutputNodes();}
    public int numOutputNodes() {return hiddenToOutput.numOutputNodes();}
    
    public Perceptron getHiddenLayer() {return inputToHidden;}
    public Perceptron getOutputLayer() {return hiddenToOutput;}
    
    public MultiLayer(int numIn, int numHid, int numOut) {
        this(new Perceptron(numIn, numHid), new Perceptron(numHid, numOut));
    }
    
    public MultiLayer(Perceptron hidden, Perceptron output) {
    	checkArgs(hidden.numOutputNodes(), output.numInputNodes(), "output");
        inputToHidden = hidden;
        hiddenToOutput = output;
    }
    
    public double[] compute(double[] inputs) {
    	checkCompute(inputs);
        return hiddenToOutput.compute(inputToHidden.compute(inputs));
    }
    
    public void updateWeights() {
        inputToHidden.updateWeights();
        hiddenToOutput.updateWeights();
    }
    
    protected void backpropagate(double[] inputs, double rate) {
        /* TODO: Calculate the backpropagated error for each hidden/output node pair.
           Then call inputToHidden.setError() and inputToHidden.addToWeightDeltas()
           to store the errors.
         */
    	
    	for(int j = 0; j < numHiddenNodes(); j++){
    		double errorHid = 0.0;
			double grad = gradient(inputToHidden.output(j));
    		for(int i = 0; i < numOutputNodes(); i++){
    			double weight = hiddenToOutput.getWeightFromTo(j, i);
        		double errorOut = hiddenToOutput.error(i);
    			errorHid += weight * grad * errorOut;
    		}
    		inputToHidden.setError(j, errorHid);
    	}
    	inputToHidden.addToWeightDeltas(inputs, rate);
    	//inputToHidden.updateWeights();
    }

    public void train(double[] inputs, double[] targets, double rate) {
    	checkTrain(inputs, targets, rate);
        hiddenToOutput.train(getHiddenLayer().compute(inputs), targets, rate);
        backpropagate(inputs, rate);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Input to Hidden\n");
        sb.append(inputToHidden);
        sb.append("\nHidden to Output\n");
        sb.append(hiddenToOutput);
        return sb.toString();
    }
}
