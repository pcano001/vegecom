package com.veisite.vegecom.ui.framework.component.table.export;

import java.io.File;
import java.io.IOException;

import javax.swing.table.TableModel;

import com.veisite.utils.tasks.SimpleProgressListener;

public interface TableExporter {
	
	public File saveTableModelToFile(TableModel model, File file, SimpleProgressListener progressListener) 
			throws IOException;

}
