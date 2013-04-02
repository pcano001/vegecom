package com.veisite.vegecom.ui.framework.component.dialogs;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Dialog simple que muestra un boton de cerrar en la parte baja
 * y muestra en el centro el panel que se le pasa como parametro
 * @author josemaria
 *
 */
public class SimpleViewDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Clave en recurso de mensajes para texto boton close
	 */
	public static final String CLOSE_BUTTON_MESSAGE_KEY = 
			"ui.components.dialogs.CloseButtonText";
	
	private JPanel contentPanel;
	private JButton closeButton;
	
	/**
	 * recurso de mensjaes i18n
	 */
	ResourceBundle resourceBundle;

	public SimpleViewDialog(Dialog owner, String title, JPanel contentPanel, ResourceBundle resourceBundle) {
		super(owner, title);
		this.contentPanel = contentPanel;
		this.resourceBundle = resourceBundle;
		initDialog();
	}

	public SimpleViewDialog(Frame owner, String title, JPanel contentPanel, ResourceBundle resourceBundle) {
		super(owner, title);
		this.contentPanel = contentPanel;
		this.resourceBundle = resourceBundle;
		initDialog();
	}
	
	public SimpleViewDialog(Window owner, String title, JPanel contentPanel, ResourceBundle resourceBundle) {
		super(owner, title);
		this.contentPanel = contentPanel;
		this.resourceBundle = resourceBundle;
		initDialog();
	}
	
	private void initDialog() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		
		ActionListener bl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPushed(e);
			}
		};
		
		String s  = "Close";
		if (resourceBundle!=null && resourceBundle.containsKey(CLOSE_BUTTON_MESSAGE_KEY)) {
			s = resourceBundle.getString(CLOSE_BUTTON_MESSAGE_KEY);
		}
		closeButton = new JButton(s);
		closeButton.addActionListener(bl);
		
		JPanel dialogPanel = new JPanel(new BorderLayout());
		dialogPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		buttonPanel.add(closeButton);
		dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
		dialogPanel.add(this.contentPanel, BorderLayout.CENTER);
		getContentPane().add(dialogPanel);
	}
	
	private void buttonPushed(ActionEvent e) {
	    JButton button = (JButton) e.getSource();
	    if (button==closeButton) {
	    	dispose();
	    }
	}
	
	/* Modificado para poder salir cuando se cierra la ventana */
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	    	closeButton.doClick();
			return;
		}
		super.processWindowEvent(e);
	}

}
