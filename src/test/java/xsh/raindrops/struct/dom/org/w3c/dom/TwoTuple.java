package xsh.raindrops.struct.dom.org.w3c.dom;

public class TwoTuple <F,S>{
	
	private F first;
	
	private S second;
	
	public TwoTuple(F first, S second) {
		super();
		this.first = first;
		this.second = second;
	}
	public F getFirst() {
		return first;
	}
	public void setFirst(F first) {
		this.first = first;
	}
	public S getSecond() {
		return second;
	}
	public void setSecond(S second) {
		this.second = second;
	}
	
	
}
