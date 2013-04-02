package com.veisite.vegecom.ui.framework.component.dialogs;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Dialog simple que muestra un boton de cerrar en la parte baja
 * y muestra en el centro el panel que se le pasa como parametro
 * @author josemaria
 *
 */
public abstract class AbstractEditDialog extends JDialog {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8937884809349987950L;
	
	/**
	 * Clave en recurso de mensajes para texto boton ok
	 */
	public static final String OK_BUTTON_MESSAGE_KEY = 
			"ui.components.dialogs.OkButtonText";
	/**
	 * Clave en recurso de mensajes para texto boton cancel
	 */
	public static final String CANCEL_BUTTON_MESSAGE_KEY = 
			"ui.components.dialogs.CancelButtonText";
	/**
	 * Clave en recurso de mensajes para pregunta de descartar cambios
	 */
	public static final String DISCARDCHANGE_QUESTION_MESSAGE_KEY = 
			"ui.components.dialogs.AbstractEditDialog.DiscardChangeQuestion";
	/**
	 * Clave en recurso de mensajes para titulo dialogo de descartar cambios
	 */
	public static final String DISCARDCHANGE_TITLE_MESSAGE_KEY = 
			"ui.components.dialogs.AbstractEditDialog.DiscardChangeTitle";

	/**
	 * Almacena la opción de salida del dialog. Puede ser:
	 * JOptionPane.NO_OPTION
	 * JOptionPane.CLOSED_OPTION  (It's equals to a CANCEL_OPTION)
	 * JOptionPane.OK_OPTION
	 * JOptionPane.CANCEL_OPTION
	 */
	private int exitCode = JOptionPane.NO_OPTION; 
	
	/**
	 * Buttons
	 */
	private JButton okButton;
	private JButton cancelButton;
	
	/**
	 * True si el contenido editable ha cambiado. se usa
	 * para mostrar confirmación en la cancelación
	 * Las subclases deberán modificar este atributo 
	 */
	protected boolean contentDirty;
	
	/**
	 * Mensajes de texto
	 */
	protected ResourceBundle resourceBundle;

	/**
	 * Compoente a añadir al dialogo
	 */
	JPanel contentPanel;
	private Component contentComponent;

	public AbstractEditDialog(Dialog owner, ResourceBundle resourceBundle) {
		super(owner);
		this.resourceBundle = resourceBundle;
		initDialog();
	}

	public AbstractEditDialog(Frame owner, ResourceBundle resourceBundle) {
		super(owner);
		this.resourceBundle = resourceBundle;
		initDialog();
	}
	
	public AbstractEditDialog(Window owner, ResourceBundle resourceBundle) {
		super(owner);
		this.resourceBundle = resourceBundle;
		initDialog();
	}
	
	public AbstractEditDialog(Component parent, ResourceBundle resourceBundle) {
		this(SwingUtilities.getWindowAncestor(parent), resourceBundle);
	}
	
	
	public Component getContentComponent() {
		return contentComponent;
	}
	
	public void setContentComponent(Component contentComponent) {
		if (this.contentComponent!=null)
			contentPanel.remove(this.contentComponent);
		this.contentComponent = contentComponent;
		contentPanel.add(contentComponent, BorderLayout.CENTER);
	}
	
	/**
	 * @return the contentDirty
	 */
	public boolean isContentDirty() {
		return contentDirty;
	}

	/**
	 * @param contentDirty the contentDirty to set
	 */
	public void setContentDirty(boolean contentDirty) {
		this.contentDirty = contentDirty;
	}

	private void initDialog() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		
		ActionListener bl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPushed(e);
			}
		};
		
		/* Panel de contenido y panel de botones */
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		if (contentComponent!=null)
			contentPanel.add(contentComponent, BorderLayout.CENTER);
		
		String s = "Ok";
		if (resourceBundle!=null && resourceBundle.containsKey(OK_BUTTON_MESSAGE_KEY)) {
			s = resourceBundle.getString(OK_BUTTON_MESSAGE_KEY);
		}
		okButton = new JButton(s);
		okButton.addActionListener(bl);
		s = "Cancel";
		if (resourceBundle!=null && resourceBundle.containsKey(CANCEL_BUTTON_MESSAGE_KEY)) {
			s = resourceBundle.getString(CANCEL_BUTTON_MESSAGE_KEY);
		}
		cancelButton = new JButton(s);
		cancelButton.addActionListener(bl);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().add(contentPanel);
	}
	
	private void buttonPushed(ActionEvent e) {
	    JButton button = (JButton) e.getSource();

	    if (button==okButton) {
	    	okButtonPushed();
	    }
	    if (button==cancelButton) {
	    	cancelButtonPushed();
	    }
	}

	private void okButtonPushed() {
		if (canClose())	{
	    	setExitCode(JOptionPane.OK_OPTION);
			closeDialog();
		}
	}
	    	
    /* Trata la pulsacion de boton de cancelar, también el cierre del dialogo */
	private void cancelButtonPushed() {
		// Si el contenido ha cambiado confirmar cambios
		if (!wantDiscardChanges()) return;
		if (canCancel()) {
	    	setExitCode(JOptionPane.CANCEL_OPTION);
			closeDialog();
		}
	}

	/* Cierra el diálogo */
	private void closeDialog() {
		this.dispose();
		onClose();
	}

	/* Modificado para poder salir cuando se cierra la ventana */
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	    	cancelButton.doClick();
			return;
		}
		super.processWindowEvent(e);
	}
	
	/**
	 * @return exitCode
	 */
	public int getExitCode() {
		return exitCode;
	}

	/**
	 * @param exitCode
	 */
	protected void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	
	/**
	 * Metodo que devuelve true cuando se puede cerrar el diálogo
	 * false en otro caso.
	 * Si devuelve true se cierra y oculta el dialogo
	 * a implementar por las subclases
	 * @return
	 */
	protected abstract boolean canClose();

	/**
	 * Metodo que devuelve true cuando se puede cerrar el diálogo
	 * cuando se pulsa el botón cancelar.
	 * false en otro caso.
	 * Si devuelve true se cierra y oculta el dialogo
	 * a implementar por las subclases
	 * @return
	 */
	protected abstract boolean canCancel();

	/**
	 * Metodo que se llama cuando se cierra el diálogo
	 * Se puede usar para realizar areas de apagado o actualizaciones
	 * cuando el dialogo es no modal.
	 * @return
	 */
	protected abstract void onClose();

	/**
	 * Pregunta si se quieren cancelar y perder los cambios
	 */
	private boolean wantDiscardChanges() {
		if (!isContentDirty()) return true;
		String q = "Do you want to discard changes?";
		if (resourceBundle!=null && resourceBundle.containsKey(DISCARDCHANGE_QUESTION_MESSAGE_KEY)) {
			q = resourceBundle.getString(DISCARDCHANGE_QUESTION_MESSAGE_KEY);
		}
		String t = "Cancel changes";
		if (resourceBundle!=null && resourceBundle.containsKey(DISCARDCHANGE_TITLE_MESSAGE_KEY)) {
			t = resourceBundle.getString(DISCARDCHANGE_TITLE_MESSAGE_KEY);
		}
		/* Pedir confirmación de cierre */
		int confirm = 
			JOptionPane.showConfirmDialog(this, q, t, JOptionPane.YES_NO_OPTION);
		if (confirm==JOptionPane.YES_OPTION) return true;
		return false;
	}


}
