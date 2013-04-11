package com.veisite.vegecom.ui.framework.component.table.export;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.utils.tasks.SimpleProgressListener;

import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class XlsExporter extends TableModelExporter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public XlsExporter() {
		super(XLS_FORMAT);
	}

	public File saveTableModelToFile(File file, TableModel model, SimpleProgressListener listener) 
			throws IOException {
		
		// Construir la hoja xls
		WritableWorkbook workbook = Workbook.createWorkbook(file); 
		WritableSheet sheet = workbook.createSheet("Sheet 1", 0);

		int columns = model.getColumnCount();
		
		if (listener!=null) {
			listener.setMaximum(model.getRowCount()+1);
			listener.init();
		}

		boolean success = false;
		try {
			// Escribir las cabeceras
			for (int i=0;i<columns;i++) {
				Label label = new Label(i, 0, model.getColumnName(i));
				try {
					sheet.addCell(label);
				} catch (WriteException we) {
					throw new IOException(we);
				}
			}
			if (listener!=null && listener.canceled()) return file;
			if (listener!=null) listener.setProgress(0);
			// Escribir los datos
			for (int r=0;r<model.getRowCount();r++) {
				for (int c=0;c<columns;c++) 
					try {
						addCell(sheet, c,r+1,model.getValueAt(r, c));
					} catch (WriteException we) {
						throw new IOException(we);
					} 
				if (listener!=null && listener.canceled()) return file;
				if (listener!=null) listener.setProgress(r+1);
			}
			
			workbook.write();
			success = true;
		} finally {
			try {
				workbook.close();
			} catch (WriteException we) {
				if (success) throw new IOException(we);
			}
		}

		if (listener!=null && !listener.canceled()) listener.end();

		return file;
	}

	private void addCell(WritableSheet sheet, int column, int row, Object value) throws WriteException {
		if (value==null) return;
		try {
			if (value instanceof Date) {
				sheet.addCell(new DateTime(column, row, (Date) value, DateTime.GMT));
			} else if (value instanceof Calendar) {
				sheet.addCell(new DateTime(column, row, ((Calendar)value).getTime(), DateTime.GMT));
			} else if (value instanceof Number) {
				sheet.addCell(new jxl.write.Number(column, row, ((Number)value).doubleValue()));
			} else if (value instanceof Boolean) {
				sheet.addCell(new jxl.write.Boolean(column, row, ((Boolean)value).booleanValue()));
			} else {
				sheet.addCell(new Label(column, row, value.toString()));
			}
		} catch (RowsExceededException ree) {
			logger.error("Error al añadir celda.",ree);
		}
	}

}
