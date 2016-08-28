package com.alex.sample;

public class Caculator {
	
	public int add(int a, int b) {
		return a + b;
	}
	
	public int subtraction(int a, int b) {
		return a - b;
	}
	
	public int Multiplication(int a, int b) {
		return a * b;
	}
	
	public int Div(int a, int b) {
		return a / b;
	}
	
	public void test() {
		System.out.println("类加载信息============");
		ClassLoader loader = this.getClass().getClassLoader();
		while(loader != null) {
			System.out.println(loader);
			loader = loader.getParent();
		}
		System.out.println(this.add(1, 12));
		System.out.println(this.subtraction(100, 12));
		System.out.println(this.Multiplication(13, 12));
		System.out.println(this.Div(10, 2));
	}

	public static void main(String[] args) {
		
		Caculator ca = new Caculator();
		ca.test();
	}

}
