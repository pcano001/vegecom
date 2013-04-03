package com.veisite.utils.dataio;

public abstract class InputFlowConsumerRunnable<T> implements Runnable {

	protected Throwable error = null;
	
	protected ObjectInputFlow<T> input;

	public abstract void run();
	
	public Throwable getError() {
		return error;
	}
	
	public ObjectInputFlow<T> getInput() {
		return input;
	}
	
	public void setInput(ObjectInputFlow<T> input) {
		this.input = input;
	}
	
}
