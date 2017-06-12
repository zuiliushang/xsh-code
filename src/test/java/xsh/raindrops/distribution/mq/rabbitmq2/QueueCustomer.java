package xsh.raindrops.distribution.mq.rabbitmq2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import xsh.raindrops.distribution.mq.rabbitmq.EndPoint;

public class QueueCustomer extends EndPoint implements Runnable , Consumer{

	public QueueCustomer(String endPointName) throws IOException, TimeoutException {
		super(endPointName);
	}
	@Override
	public void run() {
		
	}
	
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

	@Override
	public void handleDelivery(String arg0, Envelope arg1, BasicProperties arg2, byte[] arg3) throws IOException {
		// TODO Auto-generated method stub
		
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
