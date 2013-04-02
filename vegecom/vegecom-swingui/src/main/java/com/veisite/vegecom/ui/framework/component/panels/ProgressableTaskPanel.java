package com.veisite.vegecom.ui.framework.component.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.veisite.utils.tasks.ProgressEvent;
import com.veisite.utils.tasks.ProgressEventListener;
import com.veisite.utils.tasks.ProgressableTask;
import com.veisite.vegecom.ui.framework.component.VMultiLineJLabel;

public abstract class ProgressableTaskPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ProgressableTask task;
//	protected JLabel iconLabel;
	protected VMultiLineJLabel message;
	protected JProgressBar progressBar;
	
	ProgressEventListener pel = new ProgressEventListener() {
		@Override
		public void taskReport(final ProgressEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateProgress(evt);
				}
			});
		}
	};

	public ProgressableTaskPanel(ProgressableTask task) {
		super();
		this.task=task;
		initPanel();
		task.addEventListener(pel);
	}
	
	protected void initPanel() {
		// Border
		setBorder(BorderFactory.createRaisedBevelBorder());
		if (task.getTitle()!=null)
			setBorder(BorderFactory.createTitledBorder(getBorder(), task.getTitle()));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx=0;c.gridy=0;
		message = new VMultiLineJLabel(task.getJobDoing());
		message.setColumns(25);
		message.setBackground(new JLabel().getBackground());
		add(message,c);
		c.gridx=1;
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(false);
		progressBar.setPreferredSize(new Dimension(150,20));
		add(progressBar,c);
	}
	
	protected void updateProgress(ProgressEvent evt) {
		// Actualizamos el panel
		message.setText(task.getJobDoing());
		progressBar.setIndeterminate(task.isIndeterminateProgress());
		progressBar.setStringPainted(!task.isIndeterminateProgress());
		switch (evt.getType()) {
		case ProgressEvent.JOB_INIT:
			if (!task.isIndeterminateProgress())
				progressBar.setValue(evt.getProgress());
			break;
		case ProgressEvent.JOB_RUNNIG:
			if (!task.isIndeterminateProgress())
				progressBar.setValue(evt.getProgress());
			break;
		case ProgressEvent.JOB_END:
			if (!task.isIndeterminateProgress())
				progressBar.setValue(evt.getProgress());
			break;
		}
		if (task.isCanceled() || task.isError() || task.isFinished()) taskEnded();
	}
	
	protected abstract void taskEnded();

	/**
	 * @return the task
	 */
	public ProgressableTask getTask() {
		return task;
	}

}
