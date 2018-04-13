package xsh.raindrops.base;

public class InterfaceC implements InterfaceP,InterfaceP2{
	
	
	
	public static void main(String[] args) {
		InterfaceP i = new InterfaceC();
		System.out.println(i.hello());
	}

	@Override
	public String hello() {
		// TODO Auto-generated method stub
		return InterfaceP.super.hello();
	}
	
}
