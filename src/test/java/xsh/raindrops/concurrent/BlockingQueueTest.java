package xsh.raindrops.concurrent;

import java.util.concurrent.BlockingQueue;

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
public class BlockingQueueTest {
	
}


