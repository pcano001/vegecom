package com.veisite.vegecom.ui.framework.component.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.veisite.utils.tasks.ProgressableTaskDialog;
import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.ui.framework.component.panels.DefaultTableStatusBar;
import com.veisite.vegecom.ui.framework.component.table.export.ExportTableModelTask;
import com.veisite.vegecom.ui.framework.component.table.export.TableModelExporter;
import com.veisite.vegecom.ui.framework.util.DesktopSupport;

public abstract class AbstractListTablePanel<T> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractListTablePanel.class);
	
	protected AbstractListJTable<T> table;
	
	protected JPopupMenu popupMenu;
	private AbstractAction recargaDatos;
	private JMenu exportMenu;
	
	protected DefaultTableStatusBar statusBar;
	
	protected Component parent;

	/**
	 * Utilidad para fomateo
	 */
	private DecimalFormat df = new DecimalFormat();
	
	/**
	 * Recursos de mensajes i18n
	 */
	private MessageSource messageSource;
	
	/**
	 * Variable y recursos de cadena
	 */
	/**
	 * clave de texto para el mensaje de progreso de carga de datos
	 */
	public static final String LOADING_MESSAGE_KEY="ui.components.ListTablePanel.LoadingText";
	/**
	 * clave de texto para el mensaje de total de filas de datos
	 */
	public static final String TOTALROW_MESSAGE_KEY="ui.components.ListTablePanel.TotalRowText";
	/**
	 * clave de texto para el mensaje de error en carga
	 */
	public static final String ERROR_MESSAGE_KEY="ui.components.ListTablePanel.ErrorText";
	/**
	 * clave de texto para el mensaje de total filas y seleccionadas
	 */
	public static final String SELECTEDROW_MESSAGE_KEY="ui.components.ListTablePanel.SelectedRowText";
	/**
	 * clave de texto para el menu de actualizar datos
	 */
	public static final String REFRESH_MENU_KEY="ui.components.ListTablePanel.RefreshMenuText";
	/**
	 * clave de texto para el menu de exportar datos
	 */
	public static final String EXPORT_MENU_KEY="ui.components.ListTablePanel.ExportMenuText";
	/**
	 * clave de texto para el menu de exportar datos
	 */
	public static final String EXPORTODS_MENU_KEY="ui.components.ListTablePanel.ExportOdsMenuText";
	/**
	 * clave de texto para el menu de exportar datos
	 */
	public static final String EXPORTXLS_MENU_KEY="ui.components.ListTablePanel.ExportXlsMenuText";
	/**
	 * clave de texto para el error al exportar los datos
	 */
	public static final String EXPORTERROR_MESSAGE_KEY="ui.components.ListTablePanel.ExportErrorText";
	/**
	 * clave de texto para el la tarea de exportacion de datos
	 */
	public static final String EXPORTTASK_MESSAGE_KEY="ui.components.ListTablePanel.ExportTaskText";
	
	/**
	 * listsner para la carga de datos
	 */
	private DataLoadListener dataLoadListener = null;
	
	/*
	 * Variables de mensajes de texto
	 */
	private String loadingText = "Loading...";
	private String errorText = "Error...";
	private String refreshTextMenu = "Refresh data";
	private String exportTextMenu = "Export data";
	private String exportOdsTextMenu = "ODS format";
	private String exportXlsTextMenu = "XLS format";
	private String exportErrorText = "Error exporting data";
	private String exportTaskText = "Exporting data...";
	
	
	public AbstractListTablePanel(Component parent, AbstractListJTable<T> table, 
			MessageSource messageSource) throws  VegecomException {
		super();
		this.parent = parent;
		this.table = table;
		this.messageSource = messageSource;
		initComponent();
	}
	
	
	/**
	 * Metodo que se llama para inicializar los componentes
	 * Debe llamarse por las subclases
	 * @throws VegecomException 
	 */
	protected void initComponent() {
		setLayout(new BorderLayout());
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(table);
		add(sp,BorderLayout.CENTER);
		statusBar = new DefaultTableStatusBar();
		showStatusBar();
		
		if (messageSource!=null) {
			loadingText = 
				messageSource.getMessage(LOADING_MESSAGE_KEY,null,loadingText,Locale.getDefault());
			errorText = 
				messageSource.getMessage(ERROR_MESSAGE_KEY,null,errorText,Locale.getDefault());
			refreshTextMenu = 
				messageSource.getMessage(REFRESH_MENU_KEY,null,refreshTextMenu,Locale.getDefault());
			exportTextMenu = 
				messageSource.getMessage(EXPORT_MENU_KEY,null,exportTextMenu,Locale.getDefault());
			exportOdsTextMenu = 
				messageSource.getMessage(EXPORTODS_MENU_KEY,null,exportOdsTextMenu,Locale.getDefault());
			exportXlsTextMenu = 
				messageSource.getMessage(EXPORTXLS_MENU_KEY,null,exportXlsTextMenu,Locale.getDefault());
			exportErrorText = 
				messageSource.getMessage(EXPORTERROR_MESSAGE_KEY,null,exportErrorText,Locale.getDefault());
			exportTaskText = 
					messageSource.getMessage(EXPORTERROR_MESSAGE_KEY,null,exportTaskText,Locale.getDefault());
		}
		dataLoadListener = new DataLoadListener() {
			@Override
			public void dataLoadInit() {
				statusBar.startProgress();
				statusBar.setProgressText(loadingText);
			}
			@Override
			public void dataLoadEnd() {
				statusBar.stopProgress();
				Object[] args = {df.format(table.getModel().getRowCount())};
				String s = 
					messageSource.getMessage(TOTALROW_MESSAGE_KEY,args,"Total: {0} rows",Locale.getDefault());
				statusBar.setProgressText(s);
				updateStatusBar();
			}
			@Override
			public void dataLoadError(Throwable exception) {
				logger.error("Error loading data",exception);
				statusBar.stopProgress();
				statusBar.setProgressText(errorText);
				updateStatusBar();
				showDataLoadError(exception);
			}
		};
		table.getModel().addDataLoadListener(dataLoadListener);
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				updateStatusBar();
			}
		});
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateStatusBar();
			}
		});
		
		configureTable();
		configurePopupMenu();
		updateStatusBar();
	}


	public void updateStatusBar() {
		Object[] args = {df.format(table.getRowCount()),df.format(table.getSelectedRowCount())};
		String s = 
			messageSource.getMessage(SELECTEDROW_MESSAGE_KEY,args,"{0} rows ({1} selected)",Locale.getDefault());
		statusBar.setText(s);
	}
	
	/**
	 * Oculta la barra de estado.
	 */
	public void hideStatusBar() {
		remove(statusBar);
	}
	
	/**
	 * Muestra la barra de estado.
	 */
	public void showStatusBar() {
		add(statusBar,BorderLayout.SOUTH);
	}
	
	/**
	 * Metodo llamado cuando se hace doble click en la tabla.
	 */
	protected abstract void doubleClickOnTable();
	
	/**
	 * Metodo lanzado al evento de poppup
	 */
	protected void showTablePopupMenu(Point p) {
		if (popupMenu==null) return;
		if (popupMenu.getComponentCount()==0) return;
		/* Leventamos el menu de edición */
		/* Ponerlo de forma que no se oculte */
		Point ap = new Point(p);
		SwingUtilities.convertPointToScreen(ap,table);
		Dimension s = popupMenu.getPreferredSize();
		Dimension sS = Toolkit.getDefaultToolkit().getScreenSize();
		if (ap.x+s.width > sS.width) {
			ap.x = sS.width-s.width-1;
		}
		if (ap.y+s.height > sS.height-35) {
			ap.y = ap.y-s.height;
		}
		SwingUtilities.convertPointFromScreen(ap,table);
		enableDisablePopupMenu();
		popupMenu.show(table,ap.x,ap.y);
	}
	
	protected void enableDisablePopupMenu() {
		// Do nothing, override for customizing
	}
	
	protected void configureTable() {
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					doubleClickOnTable();
				}
			}
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					/* Seleccionar la fila donde el cursor */
					int row = table.rowAtPoint(e.getPoint());
					if (row != -1) {
						int sel[] = table.getSelectedRows();
						boolean esta = false;
						for (int j = 0; j < sel.length; j++)
							esta = esta || (sel[j] == row);
						if (!esta) {
							table.clearSelection();
							table.setRowSelectionInterval(row, row);
						}
					}
					showTablePopupMenu(e.getPoint());
				}
			}
			public void mouseReleased(MouseEvent e) {
			}
		});
	}
	
	protected void configurePopupMenu() {
		popupMenu = new JPopupMenu();
		if (table.getModel().isUpdateable()) {
			recargaDatos = new AbstractAction(refreshTextMenu) {
				private static final long serialVersionUID = 876879258255911796L;

				@Override
				public void actionPerformed(ActionEvent e) {
					table.getModel().refreshData();
				}
			};
			popupMenu.addSeparator();
			popupMenu.add(recargaDatos);
		}
		if (table.getModel().isExportable()) {
			if (TableModelExporter.canExport(TableModelExporter.ODS_FORMAT) || 
				TableModelExporter.canExport(TableModelExporter.XLS_FORMAT)) {
				exportMenu = new JMenu(exportTextMenu);
				AbstractAction exportOds = new AbstractAction(exportOdsTextMenu) {
					private static final long serialVersionUID = 5011377808209087337L;
	
					@Override
					public void actionPerformed(ActionEvent e) {
						exportModel(TableModelExporter.ODS_FORMAT);
					}
				};
				AbstractAction exportXls = new AbstractAction(exportXlsTextMenu) {
					private static final long serialVersionUID = -4593381485553577032L;
	
					@Override
					public void actionPerformed(ActionEvent e) {
						exportModel(TableModelExporter.XLS_FORMAT);
					}
				};
				if (TableModelExporter.canExport(TableModelExporter.ODS_FORMAT)) 
					exportMenu.add(exportOds);
				if (TableModelExporter.canExport(TableModelExporter.XLS_FORMAT)) 
					exportMenu.add(exportXls);
				popupMenu.addSeparator();
				popupMenu.add(exportMenu);
			}
		}
	}


	/**
	 * @return the table
	 */
	public AbstractListJTable<T> getTable() {
		return table;
	}


	/**
	 * @return the popupMenu
	 */
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}


	/**
	 * @return the statusBar
	 */
	public DefaultTableStatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * Exportamos el modelo a hoja de calculo en fichero temporal
	 */
	private void exportModel(int format) {
		Cursor c = table.getCursor();
		table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			TableModelExporter te = new TableModelExporter(format);
			TableViewModelWraper model = new TableViewModelWraper(table);

	        ExportTableModelTask task = new ExportTableModelTask(model, te, exportTaskText);
	        Window parent = SwingUtilities.getWindowAncestor(this);
			boolean ret = ProgressableTaskDialog.showTaskRunning(parent, exportTaskText, task);
			// Si la tarea sigue ejecutando es que se ha cancelado, salir.
			if (!ret) return;
			if (task.isCanceled()) return;
			if (task.isError()) throw task.getException();
			DesktopSupport.openFile(task.getFile());
		} catch (Throwable t) {
			logger.error("Error exporting data.",t);
			ErrorInfo info = new ErrorInfo(exportErrorText, t.getMessage(), 
					t.getMessage(), null, t, null, null);
			JXErrorPane.showDialog(this, info);
		} finally {
			table.setCursor(c);
		}
	}
	
	protected abstract void showDataLoadError(Throwable exception);
	
}
