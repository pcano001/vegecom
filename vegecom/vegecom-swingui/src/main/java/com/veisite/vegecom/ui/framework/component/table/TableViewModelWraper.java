package com.veisite.vegecom.ui.framework.component.table;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Esta clase actua como un envoltorio para el modelo real de una
 * tabla y devuelve las filas que componen la vista actual y en el orden
 * que definen el filtro de la tabla y su objeto de ordenación
 * 
 * @author josemaria
 *
 * @param <T>
 */
public class TableViewModelWraper implements TableModel {

	private JTable table; 
	
	public TableViewModelWraper(JTable table) {
		this.table = table;
	}

	@Override
	public int getColumnCount() {
		return table.getColumnCount();
	}

	@Override
	public int getRowCount() {
		return table.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int modelColumnIndex = table.convertColumnIndexToModel(columnIndex);
		int modelRowIndex = table.convertRowIndexToModel(rowIndex);
		return table.getModel().getValueAt(modelRowIndex, modelColumnIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		int modelColumnIndex = table.convertColumnIndexToModel(columnIndex);
		return table.getModel().getColumnClass(modelColumnIndex);
	}

	@Override
	public String getColumnName(int columnIndex) {
		int modelColumnIndex = table.convertColumnIndexToModel(columnIndex);
		return table.getModel().getColumnName(modelColumnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}
