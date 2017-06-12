package xsh.raindrops.distribution.mq.rabbitmq2;

/**
 * rabbitMQ其实真正的思想是生产者不发送任何信息到队列，
 * 甚至不知道信息将发送到哪个队列。
 * 相反生产者只能发送信息到交换机，交换机接收到生产者的信息，
 * 然后按照规则把它推送到对列中，交换机是如何做处理他接收到的信息，
 * 并怎么样发送到特定的队列，那么这一篇主要是讲解交换机的规则
 * @author Raindrops on 2017年6月9日
 * 采用不同的交换机规则
 */
public class Main {
	
}
