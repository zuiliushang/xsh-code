package xsh.raindrops.lock.problem;

/**
 * Java Happens-Before
 * @author Raindrops on 2017年5月25日
 */
public class HappensBefore {
	/*
		1.程序次序原则：一个线程内，写在前面的代码比写在后面的代码先发生操作
		2.锁定规则：一个unLock操作发生在后面对同一个锁进行Lock操作之前
		3.volatile变量规则：对一个变量的写操作先行发生于后面对这个变量的读操作
		4.传递规则：如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C
		5.线程启动规则：Thread对象的start()方法先行发生于此线程的每个一个动作
		6.线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
		7.线程终结规则：线程中所有的操作都先行发生于线程的终止检测，
		我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
		8.对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始
	 */
	 
}
