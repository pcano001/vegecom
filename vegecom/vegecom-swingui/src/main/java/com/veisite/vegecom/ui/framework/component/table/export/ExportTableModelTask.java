package com.veisite.vegecom.ui.framework.component.table.export;

import java.io.File;

import javax.swing.table.TableModel;

import com.veisite.utils.tasks.ProgressableTask;

public class ExportTableModelTask extends ProgressableTask {
	
	
	private File file;
	
	/**
	 * exporter
	 */
	private TableModelExporter exporter;
	
	/**
	 * tipo de firma del documeto
	 */
	private TableModel model;
	
	/**
	 * Message to show on progress
	 */
	private String message;
	
	public ExportTableModelTask(TableModel model, TableModelExporter exporter, String message) {
		this.model = model;
		this.exporter = exporter;
		this.message = message;
		if (exporter.getFormat()==TableModelExporter.XLS_FORMAT) setCancelable(true);
	}
	
	@Override
	public void doInBackground() throws Throwable {
		setIndeterminateProgress(false);
		if (message!=null) setJobDoing(message);
		this.file = exporter.exportToTempFile(model, "tmpexport",getListener());
		return;
	}

	/**
	 * @return the reportFile
	 */
	public File getFile() {
		return file;
	}

	@Override
	public boolean cancelResquestedAreYouOk() {
		return true;
	}

}
