package xsh.raindrops.struct.set;

import java.util.TreeSet;

public class TreeSetTest {
	public static void main(String[] args){
		TreeSet<String> treeSet = new TreeSet<>();
		treeSet.add("Hello");
		treeSet.add("Hello");
		System.out.println(treeSet.size());
	}
}
