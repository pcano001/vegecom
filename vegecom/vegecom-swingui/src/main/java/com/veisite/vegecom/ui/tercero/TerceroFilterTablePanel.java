package com.veisite.vegecom.ui.tercero;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import org.springframework.util.Assert;

import com.veisite.utils.StringUtil;
import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.ui.framework.component.VTextField;
import com.veisite.vegecom.ui.framework.component.table.filter.NoAcentosRegexFilter;
import com.veisite.vegecom.ui.service.TerceroUIService;

public class TerceroFilterTablePanel<T extends TerceroComercial> extends JPanel {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = 2764915812999391758L;

	/**
	 * Listener para eventos de cambio
	 */
    private EventListenerList listenerList = new EventListenerList();
    
    private VTextField searchField;
    
    /**
     * TIMEOUT es el tiempo que se espera a que no se pulse una tecla 
     * para lanzar el filtro
     */
    private static final long TIMEOUT=300L;
    /**
     * Timer que ejecutas la tarea de filtro
     */
    private Timer filterTimer;
    /**
     * Tarea que se planifica para ejecutar el filtro
     */
    private TimerTask filterTimerTask=null;
    /**
     * Objeto para controlar el acceso al timer
     */
    final Object lock = new Object();
    
    /**
     * servicio de recursos gráficos
     */
    private TerceroUIService<T> uiService;
    
	
	public TerceroFilterTablePanel(TerceroUIService<T> uiService) {
		super(new BorderLayout());
		Assert.notNull(uiService);
		this.uiService = uiService;
		setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
		filterTimer = new Timer();
		configurePanel();
	}

	private void configurePanel() {
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		String s = uiService.getMessage("ui.tercero.TerceroFilterPanel.findPrompt", 
				null, "Find/Filter:");
	 	searchField = new VTextField(s);
	 	searchField.setColumns(20);
	 	filterPanel.add(searchField);
	 	
	 	add(filterPanel, BorderLayout.CENTER);
	 	
	 	/* Bottom panel */
	 	JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		s = uiService.getMessage("ui.tercero.TerceroFilterPanel.clearButton", 
				null, "Find/Filter:");
	 	JButton clearButton =  new JButton(s);
	 	actionPanel.add(clearButton);
	 	filterPanel.add(actionPanel);
	 	//add(actionPanel, BorderLayout.SOUTH);
	 	
	 	ActionListener changeListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.setText("");
				fireFilterChanged();
			}
	 	};
	 	clearButton.addActionListener(changeListener);
	 	searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				scheduleFilterChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				scheduleFilterChanged();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				scheduleFilterChanged();
			}
		});
	}

	
	public RowFilter<TerceroListTableModel<T>, Integer> getRowFilter() {
		List<RowFilter<TerceroListTableModel<T>, Integer>> filters = 
				new ArrayList<RowFilter<TerceroListTableModel<T>, Integer>>();

		String search = searchField.getText().trim();
		if (!search.isEmpty()) {
			List<RowFilter<TerceroListTableModel<T>, Integer>> orFilter =
					new ArrayList<RowFilter<TerceroListTableModel<T>, Integer>>();;
			RowFilter<TerceroListTableModel<T>, Integer> searchFilter = 
					RowFilter.regexFilter(search);
			orFilter.add(searchFilter);
			RowFilter<TerceroListTableModel<T>, Integer> asciiFilter =
					NoAcentosRegexFilter.createNoAcentosFilter(true, 
							StringUtil.quitaAcentos(search.toUpperCase()));
			orFilter.add(asciiFilter);
			filters.add(RowFilter.orFilter(orFilter));
		}
		if (filters.isEmpty()) return null;
		RowFilter<TerceroListTableModel<T>, Integer> allFilter = RowFilter.andFilter(filters);		
		return allFilter;
	}
	
	/**
	 * Gesti�n de eventos en la lista de datos A�ade un data listener a la lista
	 * de datos
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * Elimina un data listener de la lista de datos
	 */
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/* Notify all listener */
	/* Selection changed */
	protected void fireFilterChanged() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListeners(ActionListener.class);
		ActionEvent e = new ActionEvent(this,0,"changed");

		for (int i = listeners.length - 1; i >= 0; i--)
			((ActionListener) listeners[i]).actionPerformed(e);
	}
	
	
	/**
	 * Planifica para el timeput fijado el lanzamiento de la tarea
	 * de filtrado de filas
	 */
	private void scheduleFilterChanged() {
		synchronized(lock) {
			cancelTimers();
			filterTimerTask = new FilterTimerTask();
			filterTimer.schedule(filterTimerTask, TIMEOUT);
		}
	}
	
	private void cancelTimers() {
		if (filterTimerTask!=null) {
			filterTimerTask.cancel();
		}
		filterTimer.purge();
	}
	
	private class FilterTimerTask extends TimerTask {
		@Override
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					synchronized(lock) {
						fireFilterChanged();
						cancelTimers();
					}
				}
			});
		}
	}
	
}
