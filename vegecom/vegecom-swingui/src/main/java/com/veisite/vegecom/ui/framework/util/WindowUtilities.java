package com.veisite.vegecom.ui.framework.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public class WindowUtilities {

	public static void centerWindowsOnDesktop(java.awt.Window window) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		window.setLocation((screenWidth - window.getWidth()) / 2,
				(screenHeight - window.getHeight()) / 2);
	}

}
