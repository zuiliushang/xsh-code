package xsh.raindrops.concurrent.base;

/**
 * <pre>
 * 
 * </pre>
 * @author raindrops
 * @date 2018-04-16
 *
 */
public class T02SynchronizedTest {
	
	public static void main(String[] args) {
		T02SynchronizedTest t = new T02SynchronizedTest();
		synchronized(t) {
			synchronized(t) {	
				System.out.println("made it!");
			}
		}
	}
	
}
