package xsh.raindrops.concurrent.base;


/**
 * <pre>
 * 变量x 存储在 内存中  程序为了加速 在获取变量x时候
 * 内存 -> 寄存器 -> 程序
 * 以后读取 直接是 寄存器和程序相互作用
 * 
 * 修改也是暂时修改了 寄存器中的内存变量 而不是主内存
 * 
 * {@link volatile}就是让 程序在修改 x 时候 直接修改保存在主内存
 * 
 * 总之,volatile帮我们 做了
 * 
 * 1.将当前处理器缓存行的数据写回到系统内存中
 * 
 * 2.这个写回到内存的数据会使得其他CPU（比如是多核处理器）里缓存了该内存地址的数据无效（重新从主存读取）
 * 
 * happen-before:
 * 不止是 volatile 变量受到影响
 * 在这个变量之前所有变量都会在当前程序块有volatile作用
 * <code>
 * volatile int a = 0;
 * </code>
 * 
 * </pre>
 * @author Raindrops
 * @date 2018-04-13
 *
 */
public class T01VolatileTest {
	
	private volatile int semaphore = 1;//可以去掉 volatile试试
	
	public void shutdown() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		semaphore = 2;
		System.out.println(Thread.currentThread().getName() +  "turn off");
	}
	
	public void run() {
		long i = 0;
		while (semaphore != 2) {
			i++;
		}
		System.out.println(Thread.currentThread().getName() + "machine shutdown ... 执行了" + i + "次");
	}
	
	public static void main(String[] args) throws InterruptedException {
		T01VolatileTest machine = new T01VolatileTest();
		new Thread(()->machine.run()).start();
		new Thread(()->machine.run()).start();
		new Thread(()->machine.run()).start();
		new Thread(()->machine.shutdown()).start();
		new Thread(()->machine.run()).start();
	}
}
