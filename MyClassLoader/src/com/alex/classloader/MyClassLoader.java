package com.alex.classloader;

import org.apache.bcel.Repository;

public class MyClassLoader extends ClassLoader {
	public MyClassLoader() {
		super();
//		System.out.println("无参构造器");
	}



	public MyClassLoader(ClassLoader parent) {
		super(parent);
//		System.out.println("有参构造器 parent loader: "  + parent);
//		System.out.println(Repository.class.getClassLoader());
	}


	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (name.startsWith("com.alex.sample")) {
//			System.out.println("符合条件1 name:" + name);
			byte[] data = ClassTransfor.Tranform(name);
			
			return defineClass(name, data, 0, data.length);
		}
		return super.loadClass(name, resolve);
		
	}

	public static void main(String[] args) throws Exception {
		
		
		//================================手动显示调用=================================
		//创建自定义加载器，并定义其父类加载器为null
		MyClassLoader loader = new MyClassLoader();
		//打印加载器信息
		System.out.println("自定义加载器信息，父类：" + loader.getParent());
		
		//加载测试类
		Class clazz = loader.loadClass("com.alex.sample.Caculator2");
		//打印测试类的加载器信息
		System.out.println(clazz.getClassLoader());
		
		java.lang.reflect.Method[] ms = clazz.getMethods();
		for (int i=0; i<ms.length; i++) {
			System.out.println(ms[i]);
		}
		
		Object obj = clazz.newInstance();
		System.out.println(obj);
		
		java.lang.reflect.Method method = obj.getClass().getMethod("test", null);
		method.invoke(obj, null);
		
		//================================手动显示调用=================================
		 
		
		//================================隐式调用=====================================
//		Caculator ca = new Caculator();
//		System.out.println("对象的加载器信息 " + ca.getClass().getClassLoader());
//		ca.test();
		
		
	}

}
