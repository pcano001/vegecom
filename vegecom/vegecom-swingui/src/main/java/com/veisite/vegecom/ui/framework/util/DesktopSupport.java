package com.veisite.vegecom.ui.framework.util;

import java.io.File;
import java.io.IOException;

public class DesktopSupport {
	
	public static void openPdfFile(File file) throws IOException {
		try {
			java.awt.Desktop.getDesktop().open(file);
		} catch (UnsupportedOperationException e) {
			// No se soporta la operación de apertura.
			// Si es linux se intenta con evince
			tryOpenPDFOnLinux(file);
		}
	}
	
	private static void tryOpenPDFOnLinux(File file) throws IOException {
		String os = System.getProperty("os.name").toUpperCase();
		if (os.contains("LINUX")) {
			String command = "evince "+file.getAbsolutePath();
			Runtime.getRuntime().exec(command);
			return;
		}
		throw new IOException("No se puede mostrar el fichero. Sistema operativo no soportado.");
	}

	public static void openFile(File file) throws IOException {
		try {
			java.awt.Desktop.getDesktop().open(file);
		} catch (UnsupportedOperationException e) {
			// No se soporta la operación de apertura.
			// Si es linux se intenta con evince
			tryOpenFileOnLinux(file);
		}
	}
	
	private static void tryOpenFileOnLinux(File file) throws IOException {
		String os = System.getProperty("os.name").toUpperCase();
		if (os.contains("LINUX")) {
			String command = file.getAbsolutePath();
			// Try on gnome
			try {
				Runtime.getRuntime().exec("gnome-open "+command);
				return;
			} catch (Throwable t) {}
			// Try on kde
			try {
				Runtime.getRuntime().exec("kmfclient "+command);
				return;
			} catch (Throwable t) {}
		}
		throw new IOException("Cannot show file. OS not supported.");
	}

}
