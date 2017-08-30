package xsh.raindrops.base;

public class Main02 {
	public void toName(Apple apple) {
		apple.name="haha";
	}
	
	public static void main(String[] args) {
		Apple apple = new Apple();
		apple.name="123";
		Main02 main02 = new Main02();
		main02.toName(apple);
		System.out.println(apple.name);
	}
}

class Apple{
	public String name;
}
