package xsh.raindrops.distribution.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

/**
 * @author Raindrops on 2017年5月24日
 */
public class TestUseZk {
	 private static CountDownLatch latch = new CountDownLatch(1);
	
	@Test
	public void initZK() throws IOException, InterruptedException {
		// 参数1：url列表 由,分割 如： ip:port
		// 参数2：sessionTimeout 会话超时时间 毫秒为单位
		// 参数3：watcher 用 Watcher(zookeeper,Watcher)的实现类对象来作为默认的 Wathcer时间通知处理器
		// 参数4：sessionId 代表会话的Id
		// 参数5：sessionPasswd 会话密钥 和会话ID代表唯一的会话，同时能够实现会话复用，从而达到会话恢复效果
		// 参数6：canBeReadOnly boolean 用于标识当前会话知否支持"只读模式"：默认情况下当一台机器失去一半节点的时候不处理用户请求
		ZooKeeper zooKeeper = new ZooKeeper("10.0.0.59:2181,10.0.0.58:2181,10.0.0.60:2181", 3000,event->{
			System.out.println("waiting event change:  "+ event);
			if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
				latch.countDown();
				 System.out.println("countDown");
			}
		});//会话是异步建立的
		System.out.println(zooKeeper.getState());
		System.out.println("Main Thread Event OPERTATION");
		latch.await();
		System.out.println("....finish connect");
		
		
		
		
	}
}
