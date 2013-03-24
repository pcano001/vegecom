package com.veisite.utils.tasks;

import java.util.EventObject;

public class ProgressEvent extends EventObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 368966047235439886L;
	
	/**
	 * Tipos de eventos
	 */
	/**
	 * La tarea va a acomenzar
	 */
	public static final int JOB_INIT  	= 0;
	/*
	 * La tarea ha finalizado
	 */
	public static final int JOB_END		= 1;
	/**
	 * Se ha producido un error en la ejecución de la tarea
	 * El campo excepcion contiene el problema
	 */
	public static final int JOB_ERROR		= 2;
	/**
	 * La tarea se está ejecutando y se informa del progreso
	 */
	public static final int JOB_RUNNIG	= 3;
	
	
	private int type;
	
	private int progress;
	
	private String jobDoing;
	
	private Throwable exception;

	
	public ProgressEvent(Object source, int type) {
		this(source,type,0,null);
	}
	
	public ProgressEvent(Object source, int type, int progress) {
		this(source,type,progress,null);		
	}

	public ProgressEvent(Object source, int type, int progress, Throwable exception) {
		super(source);
		if (type<JOB_INIT || type>JOB_RUNNIG) {
			throw new IllegalArgumentException("El tipo de evento no es correcto");
		}
		this.type = type;
		this.progress = Math.min(100,Math.max(0,progress));
		this.exception = exception;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	/**
	 * @return the jobDoing
	 */
	public String getJobDoing() {
		return jobDoing;
	}

	/**
	 * @param jobDoing the jobDoing to set
	 */
	public void setJobDoing(String jobDoing) {
		this.jobDoing = jobDoing;
	}
	
}
