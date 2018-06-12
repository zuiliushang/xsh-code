package xsh.raindrops.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * {@link BlockingQueue}
 * add  添加到queue中 无法容纳报异常
 * offer 尝试添加到queue中 无法容纳则返回false
 * put	尝试添加到queue中，无法容纳则阻塞直到容纳
 * take 尝试取走首位的元素，没有则阻塞等待直到有
 * poll(time) 尝试取走首位的元素，没有则等待time长时间 
 * remaining 
 * remove
 * contains
 * drainTo 一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数）， 
　　　　通过该方法，可以提升获取数据效率；不需要多次分批加锁或释放锁。
 * @author Raindrops on 2017年6月13日
 *
 */
public class ConcurrentQueueTest {
	
	/**
	 * {@link ConcurrentLinkedQueue} 无阻塞无界限队列 高并发性能最好
	 * {@link ArrayBlockingQueue} 阻塞队列 {@link LinkedBlockingQueue}
	 */
	public static void provider(ConcurrentLinkedQueue<String> queue) {
		queue.add(Thread.currentThread().getName());
		System.out.println("provider"+Thread.currentThread().getName());
	}
	
	public static void consumer(ConcurrentLinkedQueue<String> queue) {
		String s = null;
		do {s = queue.poll();}while (s == null);
		System.out.println("consume" + s);
	}
	
	public static void main(String[] args) {
		ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
				Runtime.getRuntime().availableProcessors()*2 + 1
				, Runtime.getRuntime().availableProcessors()*2 + 1
				, 0L, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<>(),
				new ThreadFactoryBuilder().setNameFormat("MainEventHandler-%d").build()
				);
		for (int i=0; i < 100 ; i++) {
			poolExecutor.execute(()->ConcurrentQueueTest.provider(queue));
			poolExecutor.execute(()->ConcurrentQueueTest.consumer(queue));
		}
		poolExecutor.shutdown();
	}
	
}



