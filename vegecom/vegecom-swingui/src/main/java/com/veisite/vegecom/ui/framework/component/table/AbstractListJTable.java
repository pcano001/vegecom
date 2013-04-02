package com.veisite.vegecom.ui.framework.component.table;

import java.util.List;

import javax.swing.JTable;

import org.jdesktop.swingx.JXTable;

/**
 * Clase que imlpementa una tabla abstracta. que hace una
 * inicialización por defecto de aspectos de configuración
 * 
 *  Por defecto se define para un tipo de objeto que es el que
 *  contiene la información que se pasa al modelo.
 * 
 * @author josemaria
 *
 */
public abstract class AbstractListJTable<T> extends JXTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor de tabla con el modelo.
	 */
	public AbstractListJTable(AbstractListTableModel<T> model) {
		super(model);
		init();
	}
	
	private void init(){
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setColumnControlVisible(true);
		setBackground(new javax.swing.plaf.ColorUIResource(250,250,250));
		setAutoCreateRowSorter(true);
		AbstractListTableModel<T> model = getModel();
		if (model!=null)
			model.configureColumns(getColumnModel());
	}
	
	/**
	 * get the model
	 */
	@SuppressWarnings("unchecked")
	public AbstractListTableModel<T> getModel() {
		return ((AbstractListTableModel<T>) super.getModel());
	}
	
	/**
	 * @param model the model to set
	 */
	public void setModel(AbstractListTableModel<T> model) {
		super.setModel(model);
		if (model!=null)
			model.configureColumns(getColumnModel());
	}

	/**
	 * Devuelve el objeto  de la fila indicada
	 * @param row
	 * @return
	 */
	public T getItemAt(int row) {
		AbstractListTableModel<T> model = getModel();
		if (model!=null)
			return model.getItemAt(row);
		return null;
	}

	/**
	 * Cambia un object por el indicado en la fila especificada
	 * @param row, tercero
	 * @return
	 */
	public void setItemAt(int row, T item) {
		AbstractListTableModel<T> model = getModel();
		if (model!=null)
			model.setItemAt(row, item);
	}

	/**
	 * Elimina un item del la fila especificada.
	 * Devuelve el item eliminado o null si no se eliminó
	 * @param row
	 * @return
	 */
	public T delItemAt(int row) {
		AbstractListTableModel<T> model = getModel();
		if (model!=null) return model.delItemAt(row);
		return null;
	}

	/**
	 * Añade un nuevo item a la tabla insertandolo al final
	 * 
	 * @param tercero
	 * @return
	 */
	public void addItem(T item) {
		AbstractListTableModel<T> model = getModel();
		if (model!=null)
			model.addItem(item);
	}

	/**
	 * Añade un nuevo item a la tabla insertandolo en la 
	 * fila indicada
	 * 
	 * @param tercero
	 * @return
	 */
	public void addItemAt(int row, T item) {
		AbstractListTableModel<T> model = getModel();
		if (model!=null)
			model.addItemAt(row, item);
	}

	
	/**
	 * Devuelve la lista que se está usando en el modelo 
	 * 
	 * @param dataList
	 */
	public List<T> getDataList() {
		return getModel().getDataList();
	}
	
	/**
	 * Cambia la lista de la tabla por la proporcionada 
	 * 
	 * @param dataList
	 */
	public void setDataList(List<T> dataList) {
		getModel().setDataList(dataList);
	}
	
}

