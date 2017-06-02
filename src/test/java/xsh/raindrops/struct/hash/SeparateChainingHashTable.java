package xsh.raindrops.struct.hash;

import java.util.LinkedList;
import java.util.List;

/**
 * 分离链接法
 * @author Raindrops on 2017年5月18日
 */
public class SeparateChainingHashTable<T> {
	
	private static final int DEFAULT_TABLE_SIZE = 101;
	
	
	/**
	 * 哈希结构
	 -----
	 |   | --> X X X 
	 ----- 
	 |   | --> X X
	 ----- 
	 */
	private List<T> [] theLists;
	
	private int currentSize;

	public SeparateChainingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}

	public SeparateChainingHashTable(int size) {
		theLists = new LinkedList[ nextPrime(size) ];
		for (int i = 0; i < theLists.length; i++) {
			theLists[i] = new LinkedList<>();
		}
	}
	
	public void insert(T t){
		List<T> haList = theLists[myHash(t)];
		if (!haList.contains(t)) {
			haList.add(t);
			
			if (++currentSize > theLists.length) {
				reHash();
			}
		}
	}
	
	public void remove(T t){
		List<T> whichList = theLists [ myHash(t) ];
		if (whichList.contains( t )) {
			whichList.remove(t);
			currentSize-- ;
		}
	}
	
	public boolean contains(T t){
		List<T> haList = theLists[myHash(t)];
		return haList.contains(t);
	}
	
	public void makeEmpty(){
		for (int i = 0; i < theLists.length; i++) 
			theLists[i].clear();
		currentSize = 0;
	}
	
	private void reHash(){
		
	}
	
	/**
	 * 得到hash值
	 */
	private int myHash(T t){
		int hashVal = t.hashCode();
		hashVal %= theLists.length;
		if (hashVal < 0) {
			hashVal += theLists.length;
		}
		return hashVal;
	}
	
	private static int nextPrime( int n ){
		return 0;
	}
	
	private static boolean isPrime( int n ){
		return false;
	}
	
}
