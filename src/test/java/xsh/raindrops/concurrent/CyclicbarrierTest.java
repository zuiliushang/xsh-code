package xsh.raindrops.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * {@link CyclicBarrier} 多线程等待
 * 当都处于等待状态了一起执行（类似于赛跑机制）
 * @author Raindrops
 * @date 2018-04-16
 *
 */
public class CyclicbarrierTest {

	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		new Thread(()->{try {
			Thread.sleep(3000);
			cyclicBarrier.await();
			System.out.println("first");
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}).start();
		new Thread(()->{try {
			cyclicBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("second");}).start();
	}
	
}
