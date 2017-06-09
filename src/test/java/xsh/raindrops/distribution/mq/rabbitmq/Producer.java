package xsh.raindrops.distribution.mq.rabbitmq;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

/**
 * 生产队列
 * @author Raindrops on 2017年6月9日
 *
 */
public class Producer extends EndPoint{

	public Producer(String endPointName) throws IOException, TimeoutException {
		super(endPointName);
	}
	
	public void sendMessage(Serializable obj) throws IOException {
		channel.basicPublish("", endPointName, null, SerializationUtils.serialize(obj));
	}

}
