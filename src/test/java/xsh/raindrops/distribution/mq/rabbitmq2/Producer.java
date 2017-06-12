package xsh.raindrops.distribution.mq.rabbitmq2;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

import xsh.raindrops.distribution.mq.rabbitmq.EndPoint;

/**
 * 
 * @author Raindrops on 2017年6月9日
 *
 */
public class Producer extends EndPoint{

	public Producer(String endPointName) throws IOException, TimeoutException {
		super(endPointName);
	}
	
	public void sendMessage(Serializable obj) throws IOException {
		channel.exchangeDeclare(endPointName, "fanout");//fanout表示分发
		channel.basicPublish("", endPointName, null, SerializationUtils.serialize(obj));
	}
	
}
