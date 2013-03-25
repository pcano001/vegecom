package com.veisite.utils.dataio;

public abstract class OutputFlowProviderRunnable<T> implements Runnable {

	protected Throwable error = null;
	
	protected ObjectOutputFlow<T> output;

	public abstract void run();
	
	public Throwable getError() {
		return error;
	}
	
	public ObjectOutputFlow<T> getOutput() {
		return output;
	}
	
	public void setOutput(ObjectOutputFlow<T> output) {
		this.output = output;
	}
	
}
