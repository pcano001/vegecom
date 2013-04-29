package com.veisite.vegecom.ui.framework.component.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.utils.dataio.ObjectFlowListener;
import com.veisite.utils.dataio.ObjectFlowProvider;

public abstract class AbstractListTableModel<T> extends AbstractTableModel 
		implements ObjectFlowListener<T> {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * logger
	 */
	private static Logger logger = LoggerFactory.getLogger(AbstractListTableModel.class);
	
	protected ObjectFlowProvider<T> dataProvider;
	
	protected List<T> dataList = new ArrayList<T>();
	
	private boolean loadingData=false;
	
	private boolean neverRefreshed=true;
	
	/**
	 * Indica si los datos modelo son exportables (ODS,XLS,etc)
	 * para uso de otras clases.
	 * Por defecto true;
	 */
	private boolean isExportable = true;
	
	/**
	 * Lista de listener a carga de datos
	 */
	private List<DataLoadListener> _listeners = new ArrayList<DataLoadListener>();

	

	public AbstractListTableModel() {
		super();
	}

	public AbstractListTableModel(List<T> dataList) {
		super();
		this.dataList = dataList;
	}
	
	public AbstractListTableModel(ObjectFlowProvider<T> dataProvider) {
		this.dataProvider = dataProvider;
		this.dataProvider.addObjectFlowListener(this);
	}

	@Override
	public int getRowCount() {
		int size=0;
		if (dataList!=null ) size = dataList.size();
		return size;
	}

	/**
	 * Las subclases deben implementar este metodo para configurar las
	 * columnas a su antojo.
	 * 
	 * @param model
	 */
	public abstract void configureColumns(TableColumnModel columnModel);

	/**
	 * Fuerza una nueva recarga de datos 
	 */
	public void refreshData() {
		if (dataProvider==null || isLoadingData()) return;
		// Recargar los datos.
		LoadThread th = new LoadThread();
		setLoadingData(true);
		fireDataLoadInit();
		th.start();
	}
	

	private void loadDataBlock(List<T> lista) {
		int init = dataList.size();
		if (lista.size()<=0) return;
		dataList.addAll(lista);
		fireTableRowsInserted(init, dataList.size()-1);
	}
	
	/**
	 * Devuelve el item de la fila indicada
	 * @param rowIndex
	 * @return
	 */
	public T getItemAt(int rowIndex) {
		T item;
		if (rowIndex>=dataList.size()) item=null;
		else item = dataList.get(rowIndex); 
		return item;
	}
	
	/**
	 * Cambia un item en la fila indicada
	 * @param rowIndex
	 * @param item
	 */
	public void setItemAt(int rowIndex, T item) {
		if (rowIndex>=dataList.size()) return;
		dataList.set(rowIndex, item);
		fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
	/**
	 * Añade un item nuevo insertándolo al final de la lista
	 * @param item
	 */
	public void addItem(T item) {
		dataList.add(item);
		fireTableRowsInserted(dataList.size()-1, dataList.size()-1);
	}
	
	/**
	 * Añade un item nuevo insertándolo en la fila indicada
	 * Si la fila no es correcta lo añade al final
	 * @param rowIndex
	 * @param item
	 */
	public void addItemAt(int rowIndex, T item) {
		if (rowIndex<0 || rowIndex>=dataList.size()) addItem(item);
		dataList.add(rowIndex, item);
		fireTableRowsInserted(rowIndex, rowIndex);
	}
	
	/**
	 * Elimina un item de la fila indicada
	 * Si la fila no es correcta no hace nada
	 * @param rowIndex
	 * @param item
	 */
	public T delItemAt(int rowIndex) {
		if (rowIndex<0 || rowIndex>=dataList.size()) return null;
		T  item = dataList.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
		return item;
	}
	
	/**
	 * Devuelve true si los datos del modelo son actualizable,
	 * es decir si tiene un proveedor de datos
	 */
	public boolean isUpdateable() {
		return dataProvider!=null;
	}

	/**
	 * @return the isExportable
	 */
	public boolean isExportable() {
		return isExportable;
	}

	/**
	 * @param isExportable the isExportable to set
	 */
	public void setExportable(boolean isExportable) {
		this.isExportable = isExportable;
	}

	/**
	 * @return the loadingData
	 */
	public boolean isLoadingData() {
		return loadingData;
	}
	
	/**
	 * @return the neverRefreshed
	 */
	public boolean isNeverRefreshed() {
		return neverRefreshed;
	}

	/**
	 * Devuelve la lista que se usa en el modelo para la 
	 * visualizacion.
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * Establece una nueva lista de datos
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
		fireTableDataChanged();
	}

	/**
	 * @param loadingData the loadingData to set
	 */
	private synchronized void setLoadingData(boolean loadingData) {
		this.loadingData = loadingData;
	}

	private class LoadThread extends Thread {
		public LoadThread() {
			super();
		}
		
		public void run() {
			try {
				dataProvider.fireStartObjectFlow();
			} catch (Throwable t) {
				logger.error("Error en carga de datos.",t);
				fireDataLoadError(t);
			} finally {
				setLoadingData(false);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see es.juntadeandalucia.empleo.mgaslab.dataio.ObjectFlowListener#objectFlowInit()
	 */
	@Override
	public void objectFlowInit() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dataList.clear();
				fireTableDataChanged();
			}
		});
	}

	/* (non-Javadoc)
	 * @see es.juntadeandalucia.empleo.mgaslab.dataio.ObjectFlowListener#objectFlowBlock(java.util.List)
	 */
	@Override
	public void objectFlowBlock(final List<T> block) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadDataBlock(block);
			}
		});
	}

	/* (non-Javadoc)
	 * @see es.juntadeandalucia.empleo.mgaslab.dataio.ObjectFlowListener#objectFlowEnd()
	 */
	@Override
	public void objectFlowEnd() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				fireDataLoadEnd();
			}
		});
	}

	/* (non-Javadoc)
	 * @see es.juntadeandalucia.empleo.mgaslab.dataio.ObjectFlowListener#objectFlowError(java.lang.Throwable)
	 */
	@Override
	public void objectFlowError(Throwable exception) {
		fireDataLoadError(exception);
	}

	public synchronized void addDataLoadListener(DataLoadListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeDataLoadListener(DataLoadListener listener) {
		_listeners.remove(listener);
	}

	/**
	 *  llamar a este metodo cuando se quiere notificar el inicio
	 *  de una carga
	 * 
	 */
	private synchronized void fireDataLoadInit() {
		Iterator<DataLoadListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((DataLoadListener) i.next()).dataLoadInit();
		}
	}

	/**
	 *  llamar a este metodo cuando se quiere notificar el fin
	 *  de una carga
	 * 
	 */
	private synchronized void fireDataLoadEnd() {
		neverRefreshed = false;
		Iterator<DataLoadListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((DataLoadListener) i.next()).dataLoadEnd();
		}
	}

	/**
	 *  llamar a este metodo cuando se quiere notificar un error
	 *  en el proceso de carga
	 * 
	 */
	private synchronized void fireDataLoadError(Throwable t) {
		Iterator<DataLoadListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((DataLoadListener) i.next()).dataLoadError(t);
		}
	}

}
