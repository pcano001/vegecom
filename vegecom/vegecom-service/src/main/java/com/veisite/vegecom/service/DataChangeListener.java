package com.veisite.vegecom.service;

/**
 * Listener para la escucha de cambios en los servicios de datos.
 * 
 * Lanza eventos en caso de adicion, modificacion y eliminacion de
 * elementos.
 * 
 * @author josemaria
 *
 */
public interface DataChangeListener<T> {
	
	public void itemAdded(T item);
	
	public void itemChanged(T item);
	
	public void itemRemoved(T item);

}
