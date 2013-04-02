package com.veisite.vegecom.ui.framework.component.table;

public interface DataLoadListener {
	
	public void dataLoadInit();
	
	public void dataLoadEnd();

	public void dataLoadError(Throwable exception);

}
