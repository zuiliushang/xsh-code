package xsh.raindrops.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * {@link CountDownLatch}主线程阻塞，多个线程的countdownlatch等于0的时候 主线程执行
 * @author Raindrops
 * @date 2018-04-16
 */
public class CountDownLatchTest {
	
	public static void main(String[] argStrings ) throws InterruptedException{
		CountDownLatch countDownLatch = new CountDownLatch(2);
		new Thread(()->{System.out.println("first");countDownLatch.countDown();}).start();
		new Thread(()->{System.out.println("second");countDownLatch.countDown();}).start();
		countDownLatch.await();
		System.out.println("ok");
	}
	
}
