package com.alex.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader {
	
	private AppJarClassLoader mcl;
	
	public JarClassLoader(File file, AppJarClassLoader parent) throws MalformedURLException {
		super(new URL[]{file.toURI().toURL()}, null);
		this.mcl = parent;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		try {
			return loadClassFromJar(name, resolve);
		} catch (ClassNotFoundException e) {
			System.out.println("JarClassLoader中的loadClass产生的异常");
			e.printStackTrace();
		}
		return mcl.loadClass(name, resolve);
	}
	
	public Class<?> loadClassFromJar(String name, boolean resolve) throws ClassNotFoundException {
		Class c = findLoadedClass(name);
		
		if (c==null || c.getClassLoader()!=this) {
			c = findClass(name);
			if (resolve) {
				resolveClass(c);
			}
		}
		return c;
	}
	

}
