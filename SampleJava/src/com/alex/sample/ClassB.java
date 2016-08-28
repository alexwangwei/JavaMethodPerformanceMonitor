package com.alex.sample;

public class ClassB {
	
	private ClassA obj;
	
	public ClassB(ClassA _obj) {
		this.obj = _obj;
	}
	
	public void print(){
		String name = obj.getName();
		int age = obj.getAge();
		System.out.println("自己" + name + age);
	}

	public static void main(String[] args) {
		ClassB b = new ClassB(new ClassA("ss", 10));
		b.print();
	}

}
