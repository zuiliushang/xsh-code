package xsh.raindrops.distribution.mq.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 读取队列的程序端 实现 Runnable 接口
 * @author Raindrops on 2017年6月9日
 */
public class QueueCustomer extends EndPoint implements Runnable,Consumer {

	public QueueCustomer(String endPointName) throws IOException, TimeoutException {
		super(endPointName);
	}

	@Override
	public void run() {
		try {
			//开启消费消息,自动获取信息
			channel.basicConsume(endPointName, true,this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * consumer注册时调用
	 */
	@Override
	public void handleConsumeOk(String paramString) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleCancelOk(String paramString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancel(String paramString) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 获得一个新的message时调用
	 */
	@Override
	public void handleDelivery(String consumerTag, Envelope env,
			BasicProperties props, byte[] body) throws IOException {
		Map map = (HashMap)SerializationUtils.deserialize(body);
	    System.out.println("Message Number "+ map.get("messagenumber") + " received.");
		
	}

	@Override
	public void handleShutdownSignal(String paramString, ShutdownSignalException paramShutdownSignalException) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRecoverOk(String paramString) {
		// TODO Auto-generated method stub
		
	}
	
	
}
