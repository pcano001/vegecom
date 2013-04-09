package com.veisite.utils.tasks;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ProgressableTaskDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProgressableTask task;
	
	private Box progressPanel;
	
	
	public ProgressableTaskDialog(Frame owner, String title, ProgressableTask task) {
		super(owner, title);
		if (task==null)
			throw new java.lang.NullPointerException("Object task nulo");
		this.task=task;
		initDialog(task);
	}

	public ProgressableTaskDialog(Dialog owner, String title, ProgressableTask task) {
		super(owner, title);
		if (task==null)
			throw new java.lang.NullPointerException("Object task nulo");
		this.task=task;
		initDialog(task);
	}

	public ProgressableTaskDialog(Window owner, String title, ProgressableTask task) {
		super(owner, title);
		if (task==null)
			throw new java.lang.NullPointerException("Object task nulo");
		this.task=task;
		initDialog(task);
	}
	
	private void addProgressableTask(ProgressableTask task) {
		TaskPanel t = new TaskPanel(task);
		progressPanel.add(t);
	}
	
	private void initDialog(ProgressableTask task) {
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		progressPanel = new Box(BoxLayout.PAGE_AXIS);
		progressPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		addProgressableTask(task);
		getContentPane().add(progressPanel);
	}
	
	@Override
	public void setVisible(boolean visible) {
		try {
			if (!task.isRunning()) task.start();
			super.setVisible(true);
		} catch (TaskException e) {
			JOptionPane.showMessageDialog(this.getOwner(), e.getMessage());
		}
	}

	/**
	 * Metodo que se avisa cuando una tarea finaliza, se cancela o tiene un error 
	 * Se ejecuta en el thread de eventos de awt
	 * @param taskPanel
	 */
	private void taskFinished() {
		// Si la tarea ha finalizado, cerramos el dialogo
		this.dispose();
	}
	
	
	private class TaskPanel extends ProgressableTaskPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TaskPanel(ProgressableTask task) {
			super(task);
		}

		@Override
		protected void taskEnded() {
			taskFinished();
		}
		
	}
		
	
	/**
	 * Metodo estático que recibe una tarea y muestra su ejecución.
	 * Devuele true si la tarea finalizó su ejecución y 
	 * false si el dialogo se cerró y la tarea sigue ejecutando.
	 */
	public static boolean showTaskRunning(Window parent, String title, ProgressableTask task) {
		ProgressableTaskDialog dialog = 
				new ProgressableTaskDialog(parent, title, task);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		return (!task.isRunning());
	}


}
