package com.veisite.vegecom.ui.auth;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public abstract class LoginPanel extends JPanel {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -4457921603137631460L;
	
	/**
	 * Controles y botones de interfaz
	 */
	private String promptMessage = "<html>Type user and password to login<html>";
	private JTextField userField;
	private JPasswordField passwdField;
	private JButton okButton;
	private JButton cancelButton;
	
	private String loginMessage = "Validating user ... ";
	private JProgressBar loginProgress;
	private JLabel loginMessageLabel;
	
	private JPanel loginMessagePanel;
	private JPanel loginProgressPanel;

	public LoginPanel() {
		this(null,null);
	}
	
	public LoginPanel(String promptMessage, String loginMessage) {
		super();
		if (promptMessage!=null) this.promptMessage = promptMessage;
		if (loginMessage!=null) this.loginMessage = loginMessage;
		initPanel();
	}
	
	private void initPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel caja = new JPanel();
		caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
		caja.setBorder(BorderFactory.createRaisedBevelBorder());
		JLabel promptLabel = new JLabel(promptMessage);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(promptLabel);
		caja.add(p);
		caja.add(Box.createVerticalStrut(10));
		
		userField = new JTextField(21);
		passwdField = new JPasswordField(21);
		JPanel cp = new JPanel();
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0; c.gridy=0;
		cp.add(new JLabel("Usuario:"),c);
		c.gridx=2;
		cp.add(userField,c);
		c.gridy=1;
		cp.add(passwdField,c);
		c.gridx=0;
		cp.add(new JLabel("Contraseña:"),c);
		c.gridx=1;
		cp.add(Box.createHorizontalStrut(10),c);
		caja.add(cp);
		
		caja.add(Box.createHorizontalStrut(3));
		
		okButton = new JButton("Aceptar");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});
		cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bp.add(okButton);
		bp.add(cancelButton);
		caja.add(bp);
		
		loginMessagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		loginMessagePanel.add(Box.createVerticalStrut(20));
		loginMessagePanel.add(Box.createHorizontalStrut(5));
		
		loginProgressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		loginProgress = new JProgressBar();
		Dimension d = loginProgress.getPreferredSize();
		loginProgress.setPreferredSize(new Dimension(100,d.height));
		loginProgress.setMaximumSize(new Dimension(100,d.height));
		loginProgress.setAlignmentX(RIGHT_ALIGNMENT);
		loginProgress.setStringPainted(false);
		loginProgress.setIndeterminate(true);
		loginMessageLabel = new JLabel(loginMessage);
		loginProgressPanel.add(loginMessageLabel);
		loginProgressPanel.add(loginProgress);
		
		add(caja);
		add(Box.createVerticalStrut(10));
		add(loginMessagePanel);
	}
	
	public void acquireFocus() {
		userField.requestFocusInWindow();
	}

	/**
	 * Muestra u oculta el mensaje y la barra de progreso
	 * para el proceso de login 
	 * 
	 * @param visible
	 */
	public void showLoginProgress(boolean visible) {
		if (visible) loginMessagePanel.add(loginProgressPanel);
		else loginMessagePanel.remove(loginProgressPanel);
		this.validate();
	}
	
	
	/**
	 * Devuelve el usuario introducido
	 */
	public String getUser() {
		String u = userField.getText();
		if (u==null) return "";
		return u.trim();
	}
	
	/**
	 * Devuelve el passsword introducido
	 */
	public String getPassword() {
		return new String(passwdField.getPassword());
	}
	
	/**
	 * Establece el mensja de progreso de login
	 */
	public void setLoginMessages(String loginMessage) {
		this.loginMessage = loginMessage;
		loginMessageLabel.setText(loginMessage);
	}
	
	/**
	 * Metodo a implementar. Se llama cuando se pulsa Aceptar
	 */
	public abstract void okAction();
	
	/**
	 * Metodo a implementar. Se llama cuando se pulsa Cancelar
	 */
	public abstract void cancelAction();

	
	/**
	 * Activa/Desactiva los componentes de usuario y contraseña 
	 */
	public void enableComponents(boolean enabled) {
		okButton.setEnabled(enabled);
		userField.setEnabled(enabled);
		passwdField.setEnabled(enabled);
	}

	public JButton getOkButton() {
		return okButton;
	}
	
}
