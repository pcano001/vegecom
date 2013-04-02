package com.veisite.vegecom.ui.context;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.veisite.utils.tasks.ProgressableTask;

public class SpringContextLoader extends ProgressableTask {

	/**
	 * Guarda los caminos de configuracion y contexto
	 */
	private String[] configSpringPaths;
	
	/**
	 * contexto
	 */
	private ClassPathXmlApplicationContext context = null;

	
	public SpringContextLoader(String[] configSpringPaths) {
		this.configSpringPaths = configSpringPaths;
	}

	@Override
	public void doInBackground() throws Throwable {
		context = new ClassPathXmlApplicationContext(configSpringPaths);
	}

	public ClassPathXmlApplicationContext getContext() {
		return context;
	}
}
