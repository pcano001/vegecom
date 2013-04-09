package com.veisite.vegecom.ui.framework.component.table.export;

import java.io.File;

import javax.swing.table.TableModel;

import com.veisite.utils.tasks.ProgressableTask;
import com.veisite.utils.tasks.SimpleProgressListener;

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
	
	
	private SimpleProgressListener listener = new SimpleProgressListener() {
		private int max = 100;
		@Override
		public void init() {
			notifyProgress(0, null);
		}
		@Override
		public void setProgress(int progress) {
			notifyProgress((progress*100)/max, null);
		}
		@Override
		public void setMaximum(int maximun) {
			if (maximun != 0) this.max=maximun;
		}
		@Override
		public void end() {
			notifyProgress(100, null);
		}
	};
	

	public ExportTableModelTask(TableModel model, TableModelExporter exporter, String message) {
		this.model = model;
		this.exporter = exporter;
		this.message = message;
	}
	
	@Override
	public void doInBackground() throws Throwable {
		setIndeterminateProgress(false);
		if (message!=null) setJobDoing(message);
		this.file = exporter.exportToTempFile(model, "tmpexport",listener);
		return;
	}

	/**
	 * @return the reportFile
	 */
	public File getFile() {
		return file;
	}

}
