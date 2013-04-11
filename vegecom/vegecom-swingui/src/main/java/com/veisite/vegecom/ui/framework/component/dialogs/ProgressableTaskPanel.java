package com.veisite.vegecom.ui.framework.component.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.context.MessageSource;

import com.veisite.utils.tasks.ProgressEvent;
import com.veisite.utils.tasks.ProgressEventListener;
import com.veisite.utils.tasks.ProgressableTask;
import com.veisite.vegecom.ui.framework.UIFramework;

public abstract class ProgressableTaskPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProgressableTask task;
	private JButton cancelButton;
	private MultiLineJLabel message;
	private JProgressBar progressBar;
	private MessageSource messageSource;
	
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

	public ProgressableTaskPanel(ProgressableTask task, MessageSource messageSource) {
		super();
		this.task=task;
		this.messageSource = messageSource;
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
		message = new MultiLineJLabel(task.getJobDoing());
		message.setColumns(25);
		message.setBackground(new JLabel().getBackground());
		add(message,c);
		c.gridx=1;
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(false);
		progressBar.setPreferredSize(new Dimension(150,20));
		add(progressBar,c);
		String t = messageSource.getMessage(UIFramework.CANCELBUTTONTEXT_MSGKEY, null, "Cancel", Locale.getDefault());
		cancelButton = new JButton(t);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelTask();
			}
		});
		c.gridy=1;c.gridx=1;
		if (task.isCancelable()) add(cancelButton,c);
	}
	
	protected void updateProgress(ProgressEvent evt) {
		// Actualizamos el panel
		message.setText(task.getJobDoing());
		progressBar.setIndeterminate(task.isIndeterminateProgress());
		progressBar.setStringPainted(!task.isIndeterminateProgress());
		switch (evt.getType()) {
		case ProgressEvent.JOB_INIT:
		case ProgressEvent.JOB_RUNNIG:
		case ProgressEvent.JOB_END:
			if (!task.isIndeterminateProgress())
				progressBar.setValue(evt.getProgress());
			break;
		}
		if (task.isCanceled() || task.isError() || task.isFinished()) taskEnded();
	}
	
	protected abstract void taskEnded();

	protected void cancelTask() {
		if (task.cancel()) {
			cancelButton.setEnabled(false);
			taskEnded();
		}
	}

	/**
	 * @return the task
	 */
	public ProgressableTask getTask() {
		return task;
	}
	
	public void setCancelText(String text) {
		if (cancelButton!=null) cancelButton.setText(text);
	}
	
	
	private class MultiLineJLabel extends JTextArea {
		private static final long serialVersionUID = 1L;
		public MultiLineJLabel(String text) {
			super(text);
			setEditable(false);
			setCursor(null);
			setOpaque(false);
			setFocusable(false);
			setFont(UIManager.getFont("Label.font"));
			setWrapStyleWord(true);
			setLineWrap(true);
		}
	}	

}
