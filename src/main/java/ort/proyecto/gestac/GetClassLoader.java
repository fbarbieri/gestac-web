package ort.proyecto.gestac;

public class GetClassLoader {

	public static ClassLoader get() {
		return Thread.currentThread().getContextClassLoader();
	}
	
}
