package com.veisite.utils.dataio;

import java.util.List;


public interface ObjectFlowListener<T> {
	
	/**
	 * Se llama cuando se inicia el envio de información por bloques
	 */
	public void objectFlowInit();

	/**
	 * Se llama para informar de la llegada un un nuevo bloque de 
	 * datos. Se le pasa la lista con los objetos.
	 */
	public void objectFlowBlock(List<T> block);

	/**
	 * Se llama para informar del fin del flujo de datos.
	 */
	public void objectFlowEnd();

	/**
	 * Se llama para informar de que se ha producido un error en 
	 * el proceso de recuperacion y envio del flujo de información
	 * Pasa el objeto excepcion que explica el error
	 */
	public void objectFlowError(Throwable exception);

}
