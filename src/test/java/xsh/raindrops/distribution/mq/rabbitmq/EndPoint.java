package xsh.raindrops.distribution.mq.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * MQ入门连接
 * @author Raindrops on 2017年6月8日
 *
 *RabbitMQ是一个开源的AMQP实现，服务器端用Erlang语言编写，
 *支持多种客户端，
 *如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等，支持AJAX。
 *用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。
 *
 *	MQ中的角色身份作用：
 *	1.RabbitMQ Server：Broker 
 *	2.Client A & B： 也叫Producer 数据的发送方
 *	3.Client 1，2，3：也叫Consumer，数据的接收方
 *
 */
public abstract class EndPoint {
	
	
	// 管道
	protected Channel channel;
	// 连接名
	protected Connection connection;
	// 消息队列名称
	protected String endPointName;

	public EndPoint(String endPointName) throws IOException, TimeoutException {
		this.endPointName = endPointName;
		
		// 创建一个连接
		ConnectionFactory factory = new ConnectionFactory();
		
		// hostname
		factory.setHost("10.0.0.59");
		factory.setVirtualHost("/");   
        factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");
		// 获取一个连接
		connection = factory.newConnection();
		
		// 创建一个通道
		channel = connection.createChannel();
		
		// 在此管道声明一个队列，如果不存在则创建它
		channel.queueDeclare(endPointName, false, false, false, null);
	}
	
	/**
	 * 关闭 channel 和 connection 非必须
	 * 默认调用
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void close() throws IOException, TimeoutException {
		this.channel.close();
		this.connection.close();
	}
	
	
	
	
}
