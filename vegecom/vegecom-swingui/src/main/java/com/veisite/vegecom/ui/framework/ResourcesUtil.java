package com.veisite.vegecom.ui.framework;

import java.net.URL;

public class ResourcesUtil {

	public static URL getResource(String resource){
	    URL url ;
	    //Try with the Thread Context Loader. 
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    if(classLoader != null){
	        url = classLoader.getResource(resource);
	        if(url != null){
	            return url;
	        }
	    }

	    //Let's now try with the classloader that loaded this class.
	    classLoader = ResourcesUtil.class.getClassLoader();
	    if(classLoader != null){
	        url = classLoader.getResource(resource);
	        if(url != null){
	            return url;
	        }
	    }
	    //Last ditch attempt. Get the resource from the classpath.
	    return ClassLoader.getSystemResource(resource);
	}
	
}
