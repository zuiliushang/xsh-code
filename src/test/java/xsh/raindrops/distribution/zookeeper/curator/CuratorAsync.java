package xsh.raindrops.distribution.zookeeper.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import xsh.raindrops.distribution.zookeeper.Constant;

/**
 * 异步接口
 * @author Raindrops on 2017年5月31日
 *
 */
public class CuratorAsync {
	
	static String path = "/curator_async";
	
	static CountDownLatch latch = new CountDownLatch(2);
	
	static ExecutorService tp = Executors.newFixedThreadPool(2);
	
	static CuratorFramework client = 
			CuratorFrameworkFactory
				.builder()
				.connectString(Constant.ZK_LINK_ADDR)
				.sessionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(5000, 3))
				.build();
	
	@Test
	public void testCuratorAsync() throws Exception{
		client.start();
		System.out.println(" Main Thread : " + Thread.currentThread().getName());
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground((client,event)->{
			System.out.println("event[code: "+ event.getResultCode() + ", type: "+event.getType() +" ]");
			System.out.println("Thread of processResult:" + Thread.currentThread().getName());
			latch.countDown();
		},tp).forPath(path, "涵哥".getBytes());
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground((client,event)->{
			System.out.println("event[code: "+ event.getResultCode() + ", type: "+event.getType() +" ]");
			System.out.println("Thread of processResult:" + Thread.currentThread().getName());
			latch.countDown();
		},tp).forPath(path, "涵哥".getBytes());
		
		latch.await();
		tp.shutdown();
	}
}
