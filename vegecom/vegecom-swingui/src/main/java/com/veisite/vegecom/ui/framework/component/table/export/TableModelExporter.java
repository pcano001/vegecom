package com.veisite.vegecom.ui.framework.component.table.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.utils.tasks.SimpleProgressListener;

public class TableModelExporter {
	
	public static final Logger logger = LoggerFactory.getLogger(TableModelExporter.class);
	
	public static final int ODS_FORMAT = 0;
	public static final int XLS_FORMAT = 1;
	
	/**
	 * mantiene informacin sobre la posibilidad de exportar en excel u odf si se encuentran
	 * las librerias adecuadas en las rutas de clases.
	 */
	private static Boolean canOdf = null;
	private static Boolean canXls = null;
	
	private int format;
	
	/**
	 * custom exporter
	 */
	private TableExporter exporter = null;
	
	public TableModelExporter(int format) throws IllegalArgumentException {
		checkFormat(format);
		if (canExport(format)) this.format = format;
		else throw new IllegalArgumentException("Cannot export to resquesting format "+format+". No library.");
		exporter = getExporter(format);
	}

	public File exportToTempFile(TableModel model, String prefix, SimpleProgressListener listener) throws IOException {
		File out = null;
		try {
			out = File.createTempFile(prefix, getSuffix() );
			logger.debug("Creando fichero temporal '{}'",out.getAbsolutePath());
			out.deleteOnExit();
		} catch (IOException ex) {
			logger.error("Error creando fichero temporal");
			throw ex;
		}
		return exportToFile(out, model, listener);
	}
	
	public File exportToFile(File file, TableModel model, SimpleProgressListener listener) 
			throws FileNotFoundException, IOException {
		return exporter.saveTableModelToFile(model, file, listener); 
	}
	
	private TableExporter getExporter(int format) {
		if (format==ODS_FORMAT) return new OdsExporter();
		if (format==XLS_FORMAT) return new XlsExporter();
		return new OdsExporter();
	}
	
	/**
	 *	Devuelve true o false si puede o no exportar en el formato solicitado 
	 * @param format
	 *
	 */
	public static boolean canExport(int format) {
		if (canOdf==null || canXls==null) discoverExportLibraries();
		if (format==ODS_FORMAT && canOdf) return true;
		if (format==XLS_FORMAT && canXls) return true;
		return false;
	}
	
	private void checkFormat(int format) throws IllegalArgumentException {
		if (format!=ODS_FORMAT && format!=XLS_FORMAT) {
			IllegalArgumentException e = 
					new IllegalArgumentException("Formato de exportacion incorrecto: "+format);
			e.fillInStackTrace();
			throw e;
		}
	}
	
	private String getSuffix() {
		if (format==ODS_FORMAT) return ".ods";
		if (format==XLS_FORMAT) return ".xls";
		return "";
	}
	
	private static void discoverExportLibraries() {
		try {
		      Class.forName("jxl.Workbook", 
		    		  false, TableModelExporter.class.getClassLoader());
		      canXls = true;
		   } catch(ClassNotFoundException e) {
			   canXls = false;
		   }
		try {
		      Class.forName("org.odftoolkit.simple.SpreadsheetDocument", 
		    		  false, TableModelExporter.class.getClassLoader());
		      canOdf = true;
		   } catch(ClassNotFoundException e) {
			   canOdf = false;
		   }
		try {
		      Class.forName("org.jopendocument.dom.spreadsheet.SpreadSheet", 
		    		  false, TableModelExporter.class.getClassLoader());
		      canOdf = true;
		   } catch(ClassNotFoundException e) {
			   canOdf = false;
		   }
		
	}
	
}
