package xsh.raindrops.lock.problem;

import java.util.concurrent.TimeUnit;

/**
 * volatile内存问题
 * @author Raindrops on 2017年5月25日
 */
public class Counter {
	
	//使用volatile还是会出现并发问题 原因是：
	/*
	 Java中每个线程都有自己的线程内存区，里面有一个JVM 栈
	 每一个线程里面都有一个JVM Stack 这个栈保存了线程运行时候
	 的变量信息。当线程访问一个对象的时候，首先通过对象的引用找到
	 对应堆中的变量的值，然后把堆内存变量load到线程本地中，建立一个
	 变量副本，之后线程的对象就不再和堆中的对象有所联系，而是直接
	 访问修改本地中对象的值。 在修改之后的某个时刻（线程退出前）将对象
	 写到对象堆中的变量
	 
	 一个变量经 volatile修饰后在所有线程中必须是同步的；任何线程中改变了它的值，
	 所有其他线程立即获取到了相同的值。
	 理所当然的，volatile修饰的变量 
	 存取时比一般变量消耗的资源要多一点，因为线程有它自己的变量拷贝更为高效。
	 
	 
	 
	 而 volatile 只是保证了你每次取出来的对象 是最新的(那个时候)
	 至于后面你怎么修改 并不能保证后面的时刻这个对象会不会被其他线程写入修改
	 */
	//private volatile static int count = 0;
	private static Integer count = 0;
	
	
	/**
	 那为什么 synchronized 有用呢？？？
	 
	 
	 */
	public synchronized static void inc() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			count ++;
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		// 同事启动1000个线程,看看实际效果
		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
                @Override
                public void run() {
                	Counter.inc();
                }
            }).start();
		}
		TimeUnit.SECONDS.sleep(5);
		//这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.count=" + Counter.count);
	}
	
}
