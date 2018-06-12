package xsh.raindrops.concurrent;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列
 * @author Raindrops
 * @date 2018-04-18
 */
public class DelayQueueTest {
	
	public static void main(String[] args) throws InterruptedException {
		DelayQueue<MyDelayTask> delayQueue = new DelayQueue<>();
		delayQueue.add(new MyDelayTask("rain", 1L, TimeUnit.SECONDS));
		delayQueue.add(new MyDelayTask("drop", 10L, TimeUnit.SECONDS));
		delayQueue.add(new MyDelayTask("roze", 5L, TimeUnit.SECONDS));
		do {
			MyDelayTask task = delayQueue.take();
			System.out.println(task.getName());
		}while (!delayQueue.isEmpty());
	}
	
}

class MyDelayTask implements Delayed {

	private String name;
	
	private Long delayTime;
	
	private TimeUnit delayTimeUnit;
	
	private Long executeTime;
	
	
	public MyDelayTask(String name, Long delayTime, TimeUnit delayTimeUnit) {
		super();
		this.name = name;
		this.delayTime = delayTime;
		this.delayTimeUnit = delayTimeUnit;
		this.executeTime = System.currentTimeMillis() + delayTimeUnit.toMillis(delayTime);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Long delayTime) {
		this.delayTime = delayTime;
	}

	public TimeUnit getDelayTimeUnit() {
		return delayTimeUnit;
	}

	public void setDelayTimeUnit(TimeUnit delayTimeUnit) {
		this.delayTimeUnit = delayTimeUnit;
	}

	public Long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Long executeTime) {
		this.executeTime = executeTime;
	}

	/**
	 * 按照延时时间长度排序
	 */
	@Override
	public int compareTo(Delayed o) {
		if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
			return 1;
		}else if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
			return -1;
		}
		return 0;
	}

	/**
	 * 延时时间长度
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	
}