package com.veisite.vegecom.service;

public interface ServiceExecutionListener {
	
	public void setMaximum(int max);
	
	public void setValue(int v);
	
	public void done();
	
}
