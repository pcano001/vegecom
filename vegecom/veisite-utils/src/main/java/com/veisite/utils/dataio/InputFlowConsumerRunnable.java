package com.veisite.utils.dataio;

public abstract class InputFlowConsumerRunnable<T> implements Runnable {

	protected Throwable error = null;
	
	protected ObjectInputFlow<T> input;

	public abstract void doRead();

	public void run() {
		if (input==null)
			throw new NullPointerException("input flow is not set yet");
		try {
			doRead();
		} finally {
			input.close();
		}
	}
	
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
