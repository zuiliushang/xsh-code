package xsh.raindrops.concurrent;

/**
 * 
 * 和hashmap对比 关键点在于 resize方法非线程安全 使得多线程对 链表修改不一样的结果
 * hashtable对比是它把修改的操作分成16部分 分别进行操作 更细粒度
 * 一般当全局表来用
 * @author raindrops
 * @date 2018-04-17
 *
 */
public class ConCurrentHashMapTest {

	public static void main(String[] args) throws InterruptedException {
		
	}
	
}
