package com.veisite.vegecom.ui.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
			tryOpenFileNative(file);
		}
	}
	
	private static void tryOpenFileNative(File file) throws IOException {
		final String os = System.getProperty("os.name").toUpperCase();
        final String[] cmdarray;
        if (os.contains("WINDOWS")) {
            cmdarray = new String[] { "cmd", "/c", "start", "\"\"", file.getCanonicalPath() };
        } else if (os.contains("MAC OS")) {
            cmdarray = new String[] { "open", file.getCanonicalPath() };
        } else if (os.contains("LINUX")) {
            cmdarray = new String[] { "xdg-open", file.getCanonicalPath() };
        } else {
            throw new IOException("unknown way to open " + file);
        }
        try {
            // can wait since the command return as soon as the native application is launched
            final int res = Runtime.getRuntime().exec(cmdarray).waitFor();
            if (res != 0)
                throw new IOException("error (" + res + ") executing " + Arrays.asList(cmdarray));
        } catch (InterruptedException e) {
            throw new IOException("interrupted waiting for " + Arrays.asList(cmdarray));
        }
	}

}
