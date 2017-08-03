package xsh.raindrops.distribution.mq.rabbitmq;

import java.util.HashMap;

/**
 * 在下面的测试类中，先运行一个消费者线程，然后开始产生大量的消息，这些消息会被消费者取走。
 * @author Raindrops on 2017年6月9日
 */
public class Main {
	
	public static void main(String[] args) throws Exception {
		QueueCustomer customer = new QueueCustomer("Raindrops");
		Thread comsumerThread = new Thread(customer);
		comsumerThread.start();
		
		Producer producer = new Producer("Raindrops");
		
		for (int i = 0; i < 100000; i++) {
			HashMap map = new HashMap<>();
			map.put("messagenumber", i);
			producer.sendMessage(map);
			System.out.println("Message Number" + i + " sent.");
		}
		
	}
	
}
