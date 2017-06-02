package xsh.raindrops.struct.stack;

/**
 * 使用数组的方式实现Stack
 * @author Raindrops on 2017年5月16日
 */
public class StackArray2<T> implements Stack<T>{

	private int LEN = 8; // 堆栈默认大小 8
	
	private Object[] elements;// 数据数组
	
	
	
	public StackArray2() {
		elements = new Object[LEN];
	}

	@Override
	public int getSize() {
		return elements.length;
	}

	@Override
	public boolean isEmpty() {
		return getSize()==0;
	}

	@Override
	public void push(T t) {
		
	}

	@Override
	public T pop() throws StackEmptyException {
		return null;
	}

	@Override
	public T peek() throws StackEmptyException {
		return null;
	}

}
