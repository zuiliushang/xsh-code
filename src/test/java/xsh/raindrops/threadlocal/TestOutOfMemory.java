package xsh.raindrops.threadlocal;

import org.apache.log4j.helpers.ThreadLocalMap;

import scala.reflect.generic.Trees.New;

public class TestOutOfMemory {
	
	public static void main(String[] args) {
		ThreadLocal<byte[]> th = new ThreadLocal<>();
		th.set(new byte[1024 * 1024 * 50]);//50M
	}
	
}
