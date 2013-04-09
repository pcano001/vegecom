package com.veisite.utils.tasks;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ProgressableTaskListDialog extends JDialog {
	
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ProgressableTask> taskList =	new ArrayList<ProgressableTask>();
	
	private Box progressPanel;
	
	
	public ProgressableTaskListDialog(Frame owner, String title, ProgressableTask task) {
		super(owner, title);
		if (task==null)
			throw new java.lang.NullPointerException("Object task nulo");
		initDialog(task);
	}

	public ProgressableTaskListDialog(Dialog owner, String title, ProgressableTask task) {
		super(owner, title);
		if (task==null)
			throw new java.lang.NullPointerException("Object task nulo");
		initDialog(task);
	}

	public ProgressableTaskListDialog(Window owner, String title, ProgressableTask task) {
		super(owner, title);
		if (task==null)
			throw new java.lang.NullPointerException("Object task nulo");
		initDialog(task);
	}
	
	public void addProgressableTask(ProgressableTask task) {
		taskList.add(task);
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
		// Lanzar un thread por casa tarea a ejecutar.
		try {
			for (ProgressableTask t : taskList) {
				t.start();
			}
			super.setVisible(visible);
		} catch (TaskException e) {
			JOptionPane.showMessageDialog(this.getOwner(), e.getMessage());
		}
	}


	/**
	 * Metodo que se avisa cuando una tarea finaliza, se cancela o tiene un error 
	 * Se ejecuta en el thread de eventos de awt
	 * @param taskPanel
	 */
	public void taskFinished(ProgressableTask task) {
		// Si todas las tareas han finalizado, cerramos el dialogo
		boolean close = true;
		for (ProgressableTask tp : taskList) {
			close = close && (tp.isCanceled() || tp.isFinished() || tp.isError());
		}
		if (close) this.dispose();
	}
	
	
	private class TaskPanel extends ProgressableTaskPanel {
		/**
		 * serial
		 */
		private static final long serialVersionUID = 5511388846852092134L;

		public TaskPanel(ProgressableTask task) {
			super(task);
		}

		@Override
		protected void taskEnded() {
			taskFinished(this.getTask());
		}
	}

	
	/**
	 * Metodo estático que recibe una lista de tareas y muestra su ejecución.
	 * Devuele true si las tareas finalizaron su ejecución y 
	 * false si el dialogo se cerró y alguna de las tareas sigue ejecutandonse.
	 */
	public static boolean showTaskListRunning(Window parent, String title, List<ProgressableTask> taskList) {
		if (taskList==null || taskList.size()<=0) return true;
		ProgressableTaskListDialog dialog = 
				new ProgressableTaskListDialog(parent, title, taskList.get(0));
		for (int i=1;i<taskList.size();i++)
			dialog.addProgressableTask(taskList.get(i));
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		boolean ret = true;
		for (ProgressableTask t : taskList) ret = ret && !t.isRunning();
		return ret;
	}

	
}
