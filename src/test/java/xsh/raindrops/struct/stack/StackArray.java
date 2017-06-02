package xsh.raindrops.struct.stack;

import java.util.Vector;

/**
 * 利用 Vector 来实现 Stack
 * @author Raindrops on 2017年5月16日
 * @param <T>
 */
public class StackArray<T> implements Stack<T>{
	
	private Vector<T> vector ;
	

	public StackArray() {
		vector = new Vector<>();
	}

	@Override
	public int getSize() {
		synchronized (vector) {
			return vector.size();
		}
	}

	@Override
	public  boolean isEmpty() {
		synchronized (vector) {
			return vector.isEmpty();
		}
	}

	@Override
	public  void push(T t) {
		synchronized (vector) {
			vector.addElement(t);
		}
	}

	@Override
	public  T pop() throws StackEmptyException {
		synchronized (vector) {
			T ob;
			if (vector.isEmpty()) {
				throw new StackEmptyException();
			}
			ob = vector.get(vector.size()-1);
			vector.removeElementAt(vector.size()-1);
			return ob;
		}
	}

	@Override
	public T peek() throws StackEmptyException {
		synchronized (vector) {
			T ob ;
			if (vector.isEmpty()) {
				throw new StackEmptyException();
			}
			ob = vector.get(vector.size()-1);
			return ob;
		}
	}
	
	public static void main(String[] args){
		StackArray<Integer> stackArray = new StackArray<>();
		System.out.println(stackArray.getSize());
		stackArray.push(1);
		stackArray.push(2);
		System.out.println(stackArray.peek());
		System.out.println(stackArray.pop());
		System.out.println(stackArray.pop());
		//System.out.println(stackArray.pop());
		
	}
	
}
