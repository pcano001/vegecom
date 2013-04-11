package com.veisite.utils.tasks;

public interface SimpleProgressListener {
	
	public void init();
	
	/**
	 * By convention, maximun < 0 must be considered as an
	 * indeterminate progress task
	 *  
	 * @param maximun
	 */
	public void setMaximum(int maximun);
	
	public void setProgress(int progress);
	
	public void end();
	
	public boolean canceled();
	
}
