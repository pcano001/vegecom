package com.veisite.utils.cache;

import java.util.Date;
import java.util.List;

import com.veisite.utils.bean.Bean;

public abstract class AbstractCacheableDataService<T> extends Bean {
	
	/**
	 * Momento en milisegundos de la ultima carga de datos 
	 */
	private long lastLoadTime = 0;
	
	private List<T> cacheList;
	
	/**
	 * Número de segundo de validez de la cache de datos
	 * Por defecto 5 minutos
	 */
	private long evictionTime = 300000L;
	
	
	public long getLastLoadTime() {
		return lastLoadTime;
	}


	public void setLastLoadTime(long newLastLoadTime) {
		Long oldValue = this.lastLoadTime;
		this.lastLoadTime = newLastLoadTime;
		pcs.firePropertyChange("lastLoadTime", oldValue, newLastLoadTime);
	}

	/**
	 * Establece a este momento la carga de datos
	 */
	protected void setLoadTime() {
		Long oldValue = this.lastLoadTime;
		this.lastLoadTime = (new Date()).getTime();
		pcs.firePropertyChange("lastLoadTime", oldValue, this.lastLoadTime);
	}


	/**
	 * @return the evictionTime
	 */
	public long getEvictionTime() {
		return evictionTime;
	}


	/**
	 * @param evictionTime the evictionTime to set
	 */
	public void setEvictionTime(long newEvictionTime) {
		Long oldValue = this.evictionTime;
		this.evictionTime = newEvictionTime;
		pcs.firePropertyChange("evictionTime", oldValue, newEvictionTime);
	}

	
	/**
	 * Metodo que devuelve true si ha pasado el tiempo de caducidad de la cache
	 * now-lastLoadTime>evictionTime
	 */
	public boolean isCaducated() {
		long now = (new Date()).getTime();
		return ((now-lastLoadTime)>evictionTime);
	}

	/**
	 * @return the cacheList (intenta reload cache si caducada)
	 */
	public List<T> getCacheList() {
		if (isCaducated()) {
			refreshCache();
		}
		return cacheList;
	}
	
	/**
	 * Force the caducity of the cache and a data reload on
	 * next demand 
	 */
	public void invalidateCache() {
		lastLoadTime = 0;
	}
	
	/**
	 * @return the cacheList (sin recarga, caducada o no)
	 */
	protected List<T> getCurrentCacheList() {
		return cacheList;
	}
	
	/**
	 * invalida la cache y fuera una nueva carga
	 */

	/**
	 * @param cacheList the cacheList to set
	 */
	protected void setCacheList(List<T> newCacheList) {
		List<T> oldValue = this.cacheList;
		this.cacheList = newCacheList;
		pcs.firePropertyChange("cacheList", oldValue, newCacheList);
	}
	
	protected abstract List<T> loadCacheList();
	
	/**
	 * Fuera una recarga de la cache
	 */
	protected void refreshCache() {
		List<T> lista = loadCacheList();
		setCacheList(lista);
		setLoadTime();
	}
	
}
