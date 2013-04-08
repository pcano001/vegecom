package com.veisite.vegecom.ui.framework.component.table.export;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.table.TableModel;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdsExporter {
	
	private static final Logger logger = LoggerFactory.getLogger(OdsExporter.class);

	public static File saveTableModelToFile(TableModel model, File file) throws IOException {
		
		SpreadsheetDocument outputDocument;
		try {
			outputDocument = SpreadsheetDocument.newSpreadsheetDocument();
		} catch (Exception e) {
			logger.error("Cannot create new ods spreadsheet.",e);
			throw new IOException(e);
		}
		
		int columns = model.getColumnCount();
		
		Table sheet = outputDocument.getSheetByIndex(0);
		sheet.appendColumns(model.getColumnCount());
		sheet.appendRows(model.getRowCount()+1);
		
		// Escribir las cabeceras
		for (int i=0;i<columns;i++) {
			Cell cell = sheet.getCellByPosition(i, 0);
			cell.setStringValue(model.getColumnName(i));
		}
		// Escribir los datos
		for (int c=0;c<columns;c++) 
			for (int r=0;r<model.getRowCount();r++)
				addCell(sheet, c,r+1,model.getValueAt(r, c));
		
		try {
			outputDocument.save(file);
		} catch (Exception e) {
			throw new IOException(e);
		}
		return file;
	}

	private static void addCell(Table sheet, int column, int row, Object value) {
		if (value==null) return;
		Cell cell = sheet.getCellByPosition(column, row);
		if (value instanceof Date) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime((Date)value);
			cell.setDateValue(c);
		} else if (value instanceof Calendar) {
			cell.setDateValue((Calendar)value);
		} else if (value instanceof Number) {
			cell.setDoubleValue(((Number)value).doubleValue());
		} else if (value instanceof Boolean) {
			cell.setBooleanValue((Boolean)value);
		} else {
			cell.setStringValue(value.toString());
		}
	}

}
