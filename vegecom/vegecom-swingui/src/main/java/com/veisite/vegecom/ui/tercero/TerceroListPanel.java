package com.veisite.vegecom.ui.tercero;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.springframework.util.Assert;

import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.ui.error.ErrorUtil;
import com.veisite.vegecom.ui.framework.component.table.AbstractListJTable;
import com.veisite.vegecom.ui.framework.component.table.AbstractListTablePanel;
import com.veisite.vegecom.ui.framework.views.UIFrameworkView;
import com.veisite.vegecom.ui.service.TerceroUIService;

public abstract class TerceroListPanel<T extends TerceroComercial> extends UIFrameworkView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Panel para el filtrado de beneficiarios en la tabla.
	 */
	protected TerceroFilterTablePanel<T> filterPanel;
	/**
	 * Panel de la tabla de terceros
	 */
	protected AbstractListTablePanel<T> tablePanel;
	
	/**
	 * Opciones de menu contextual
	 */
	protected JMenuItem newTerceroMenu;
	protected JMenuItem editTerceroMenu;
	protected JMenuItem deleteTerceroMenu;
	
	/**
	 * recursos graficos
	 */
	private TerceroUIService<T> uiService;
	
	
	public TerceroListPanel(String id, TerceroUIService<T> uiService) 
			throws VegecomException {
		super(id);
		Assert.notNull(uiService);
		this.uiService = uiService;
		initComponent();
	}
	

	/**
	 * Creamos los componentes del Panel
	 * @throws GaslabException 
	 */
	protected void initComponent() throws VegecomException {
		setLayout(new BorderLayout());
		TerceroListTableModel<T> dataModel = uiService.getListTableModel();
		TerceroListJTable<T> table =
				new TerceroListJTable<T>(dataModel);
		tablePanel = new AbstractListTablePanel<T>(this,table,uiService.getMessageSource()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void doubleClickOnTable() {
				doubleClickOnTercero();
			}
			@Override
			protected void enableDisablePopupMenu() {
				super.enableDisablePopupMenu();
				enablePopupMenu();
			}
			@Override
			protected void showDataLoadError(Throwable exception) {
				dataLoadError(exception);
			}
		};
		configurePopupMenu();
		add(tablePanel,BorderLayout.CENTER);
		configureFilter();
		initSortOrder();
		if (dataModel.isNeverRefreshed()) dataModel.refreshData();
	}

	/**
	 * Configura y pone visible el panel de filtro de beneficiarios
	 */
	private void configureFilter() {
		filterPanel = new TerceroFilterTablePanel<T>(uiService);
		add(filterPanel,BorderLayout.NORTH);
		filterPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyTableFilter();
			}
		});
	}
	
	private void applyTableFilter() {
		AbstractListJTable<T> table = tablePanel.getTable();
		table.setRowFilter(filterPanel.getRowFilter());
		tablePanel.updateStatusBar();
	}
	
	private void initSortOrder() {
		AbstractListJTable<T> table = tablePanel.getTable();
		SortKey sk = new SortKey(1, SortOrder.ASCENDING);
		List<SortKey> keys = new ArrayList<SortKey>(); 
		keys.add(sk);
		table.getRowSorter().setSortKeys(keys);
	}
	
	/**
	 * Configura el menu contextual para la edicion, alta y 
	 * borrado de terceros.
	 */
	protected void configurePopupMenu() {
		JPopupMenu pm = tablePanel.getPopupMenu();
		
		newTerceroMenu = new JMenuItem(new NewTerceroAction());
		editTerceroMenu = new JMenuItem(new EditTerceroAction());
		deleteTerceroMenu = new JMenuItem(new DeleteTerceroAction());
		
		pm.add(deleteTerceroMenu,0);
		pm.add(editTerceroMenu,0);
		pm.add(newTerceroMenu,0);
	}

	/**
	 * Activa/desactiva opciones de menu
	 */
	protected void enablePopupMenu() {
		AbstractListJTable<T> table = tablePanel.getTable();
		editTerceroMenu.setEnabled(table.getSelectedRowCount()==1);
		deleteTerceroMenu.setEnabled(table.getSelectedRowCount()==1);
	}

	/**
	 * Se ejecuta al hacer doble click en un tercero
	 * Por defecto se intenta editar la fila.
	 * Se puede sobreescribir el metodo para conseguir otro
	 * comportamiento, por ejemplo, seleccionar un tercero y 
	 * salir.
	 */
	protected void doubleClickOnTercero() {
		if (editTerceroMenu!=null)
			editTerceroMenu.doClick();
	}
	
	/**
	 * Devuelve el tercero que está seleccionado en la tabla
	 * null si no hay seleccion
	 */
	public T getSelectedTercero() {
		int rowView = tablePanel.getTable().getSelectedRow();
		if (rowView<0) return null;
		int row = tablePanel.getTable().convertRowIndexToModel(rowView);
		return tablePanel.getTable().getItemAt(row);
	}
	
	/**
	 * Metodo a implementar para dar de alta un nuevo tercero
	 * Devuelve el tercero si se dio de alta correctamente y null
	 * si no se dió de alta
	 */
	protected abstract T doNewTercero(Component parent);
	
	/**
	 * Metodo a implementar para eliminar un tercero
	 * Devuelve true se se dió de baja correctamente y 
	 * false en otro caso
	 */
	protected abstract boolean doDelTercero(Component parent, T tercero);
	
	/**
	 * Metodo a implementar para editar un tercero
	 * Devuelve el Tercero si se modifico correctamente y 
	 * null en otro caso
	 */
	protected abstract T doEditTercero(Component parent, T tercero);
	
	
	/**
	 * Implementacion de acciones de menu
	 */
	/**
	 * Da de alta un nuevo tercero
	 * 
	 * @author josemaria
	 */
	private class NewTerceroAction extends AbstractAction {
		/**
		 * serial
		 */
		private static final long serialVersionUID = 3651293287585063125L;

		public NewTerceroAction() {
			super(uiService.getMessage("ui.components.menu.New", null, "New"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			doNewTercero(tablePanel);
		}
	}
	
	/**
	 * Editar un tercero
	 * 
	 * @author josemaria
	 */
	private class EditTerceroAction extends AbstractAction {
		/**
		 * serial
		 */
		private static final long serialVersionUID = -4731589874417212249L;

		public EditTerceroAction() {
			super(uiService.getMessage("ui.components.menu.Edit", null, "Edit"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AbstractListJTable<T> table = tablePanel.getTable();
			if (table.getSelectedRowCount()!=1) return;
			int row = table.convertRowIndexToModel(table.getSelectedRow());
			T tercero = table.getItemAt(row);
			if (tercero==null) return;
			doEditTercero(tablePanel, tercero);
		}
	}
	
	/**
	 * Eliminar uno o varios terceros
	 * 
	 * @author josemaria
	 */
	private class DeleteTerceroAction extends AbstractAction {
		/**
		 * serial
		 */
		private static final long serialVersionUID = 7372447598794037254L;

		public DeleteTerceroAction() {
			super(uiService.getMessage("ui.components.menu.Delete", null, "Delete"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AbstractListJTable<T> table = tablePanel.getTable();
			if (table.getSelectedRowCount()!=1) return;
			// Vamos borrando cada uno de los terceros seleecionados
			for (int rowView : table.getSelectedRows()) {
				int row = table.convertRowIndexToModel(rowView);
				T tercero = table.getItemAt(row);
				if (tercero!=null) doDelTercero(tablePanel, tercero);
			}
		}
	}

	private void dataLoadError(Throwable exception ) {
		String loadErrorText = "Error loading data...";
		loadErrorText = 
				uiService.getMessage("ui.components.ListTablePanel.LoadErrorTitleText",null,"Error loading data...");
		ErrorInfo err = ErrorUtil.getErrorInfo(exception,loadErrorText,null);
		JXErrorPane.showDialog(this, err);
	}


}
