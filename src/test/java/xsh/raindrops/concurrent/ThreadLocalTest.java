package xsh.raindrops.concurrent;

/**
 * 
 * @author raindrops
 * @date 2018-04-16
 *
 */
public class ThreadLocalTest {
	
	ThreadLocal<Long> longLocal = new ThreadLocal<Long>() {
		@Override
		protected Long initialValue() {
			return 12345L;
		}
	};
	
	ThreadLocal<String> stringLocal = new ThreadLocal<String>();
	
	public void init() {
		
	}
	
	public void set() {
		longLocal.set(Thread.currentThread().getId());
		stringLocal.set(Thread.currentThread().getName());
	}
	
	public void println() {
		System.out.println("Thread info [ id = "+longLocal.get()+", name = " + stringLocal.get()+" ]");
	}
	
	public static void main(String[] args) {
		ThreadLocalTest test = new ThreadLocalTest();
		test.init();
		//第一次设置
		test.set();
		test.println();
		new Thread(()->{test.println();test.set();test.println();}).start();
		test.println();
	}
	
}
