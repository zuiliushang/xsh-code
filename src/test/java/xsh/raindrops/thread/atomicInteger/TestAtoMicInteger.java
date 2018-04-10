package xsh.raindrops.thread.atomicInteger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAtoMicInteger {
	
	public static void main(String[] args) {
		AtomicInteger aInteger = new AtomicInteger(0);
		ExecutorService service = Executors.newFixedThreadPool(4);
		service.submit(()->{
			for (int i = 0; i < 3; i++) {
				System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                         + aInteger.incrementAndGet() + "]");  
			}
		});
		service.submit(()->{
			for (int i = 0; i < 3; i++) {
				System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                         + aInteger.incrementAndGet() + "]");  
			}
		});
		service.submit(()->{
			for (int i = 0; i < 3; i++) {
				System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                         + aInteger.incrementAndGet() + "]");  
			}
		});
		service.shutdown();
		
		new Thread(()->{
			for (int i = 0; i < 11; i++) {
				aInteger.set(i);
				System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                         + aInteger.get() + "]");    
			}
		}).start();
		new Thread(()->{
			for (int i = 0; i < 3; i++) {
				aInteger.set(i);
				System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                         + aInteger.get() + "]");  
			}
		}).start();
		new Thread(()->{
			for (int i = 0; i < 3; i++) {
				aInteger.set(i);
				System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                         + aInteger.get() + "]");  
			}
		}).start();
	}
	
}
