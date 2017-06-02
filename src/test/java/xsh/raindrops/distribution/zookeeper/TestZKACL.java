package xsh.raindrops.distribution.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

/**
 * 通过设置ZK服务器上数据节点的ACL，来控制客户端对该数据节点
 * 的访问权限：
 * 如果一个客户端符合该ACL控制才可对其进行操作。
 * ZK提供了多种权限控制模式Scheme，分别是：
 * world,auth,digest,ip,super 
 * @author Raindrops on 2017年5月24日
 */
public class TestZKACL {
	// addAuthInfo(String scheme, byte[] auth)
	// scheme world.....
	// auth 具体的权限信息
	
	private static CountDownLatch latch = new CountDownLatch(1);
	
	private ZooKeeper zooKeeper;
	
	private static final String PATH="/zk-book-auth_test";
	
	@Before
	public void init() throws IOException {
		zooKeeper 
		 	= new ZooKeeper(Constant.ZK_LINK_ADDR, 5000, event->{
		 		System.out.println("Zookeeper receive Event " + event);
		 		if (KeeperState.SyncConnected == event.getState()) {
					latch.countDown();
				}
		 	}) ;
	}
	
	/**
	 * 使用包含权限信息的ZK会话创建数据节点
	 * @throws Exception
	 */
	@Test
	public void authSample() throws Exception{
		latch.await();
		zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
		/**
		 * 采用了digest模式，包含的具体权限信息是 foo:true
		 * 很类似username:password的格式。完成权限信息的添加后
		 * 该代码还使用客户端会话在ZK上创建了 /zk-book-auth_test节点
		 * 这样该节点就成功加了权限控制啦
		 */
		zooKeeper.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	@Test
	public void readPathWithoutACL() throws Exception{
		latch.await();
		zooKeeper.getData(PATH, false, null);
		// KeeperErrorCode = NoAuth for /zk-book-auth_test
	}
	
	
}
