package xsh.raindrops.concurrent;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyArrayBlockingQueue<T> extends AbstractQueue<T>{

	/**
	 * 数据
	 */
	final Object[] items;
	
	final ReentrantLock lock;
	
	int takeIndex;
	
	int putIndex;
	
	int count;
	
	Condition notEmpty;
	
	Condition notFull;
	
	public MyArrayBlockingQueue(int capacity) {
		this(capacity,false);
	}
	
	public MyArrayBlockingQueue(int capacity, boolean b) {
		if (capacity <= 0) 
			throw new IllegalArgumentException();
		this.items = new Object[capacity];
		lock = new ReentrantLock(b);
		notEmpty = lock.newCondition();
		notFull = lock.newCondition();
	}

	@Override
	public boolean offer(T e) {
		return false;
	}

	@Override
	public T poll() {
		return null;
	}

	@Override
	public T peek() {
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

}
