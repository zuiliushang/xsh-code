package xsh.raindrops.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public  class Main {
	
	
	
	public  static <T> void main(String[] args) throws IOException, InterruptedException {
		/*A a = new A();
		B b = new B();
		List objects = new ArrayList<>();
		objects.add(a);
		objects.add(b);
		System.out.println(objects.size());
		
		System.out.println("hello world");*/
		System.out.println(Main.testReturn());;
	}
	
	
	public static int testReturn() {
		try {
			return 1;
		}catch (Exception e) {
				return 3;
			}
		 finally {
			// TODO: handle finally clause
			return 2;
		}
	}
	
	/*public  static <T>  List init(T[] tArr) {
		Class clazz;
		for (int i = 0; i < tArr.length; i++) {
			clazz = Class.forName()
		}
		return null;
	}*/
}
class A{}
class B{}