package com.veisite.utils.tasks;

public interface SimpleProgressListener {
	
	public void init();
	
	public void setMaximum(int maximun);
	
	public void setProgress(int progress);
	
	public void end();
	
}
