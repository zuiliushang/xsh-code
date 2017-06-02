package xsh.raindrops.distribution.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

/**
 * ZooKeeper的各种操作
 * @author Raindrops on 2017年5月24日
 */
public class TestZKOperation {
	
	private static CountDownLatch latch = new CountDownLatch(1);
	
	private ZooKeeper zooKeeper;
	
	
	
	
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
	 * 
	 * 创建操作 create(path,data[],acl,createMode,cb,ctx)
	 * 参数1.path：需要创建的数据节点的节点路径，例如：/zk-node/foo
	 * 参数2.data[]：节点数据
	 * 参数3.acl：节点的ACL策略
	 * 参数4.createMode：节点类型，一个枚举类型，4种类型的选择来决定节点的持久时间：
	 * 					持久(PERSISTENT)、持久排序(PERSISTENT_SEQUENTIAL)、临时(EPHEMERAL)、临时顺序(EPHEMERAL_SEQUENTIAL)
	 * 参数5.cb：注册一个异步回调函数。开发人员需要实现一个 StringCallback接口，主要是对下面方法进行重写
	 * 			void processResult(int rc,String path,Object ctx,String name);
	 * 参数6.ctx：用于传递一个对象，可以在回调方法执行的时候使用，通常放在一个上下文(Context) 信息
	 * 同步创建
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	@Test
	public void testCreateSync() throws InterruptedException, KeeperException {
		latch.await();
		String path1 = zooKeeper.create("/Rotos", "社会我ROTOS哥,人狠话不多.".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Success create znode " + path1);
		String path2 = zooKeeper.create("/Xusihan", "涵哥".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create znode " + path2);
	}
	
	
	/**
	 * 返回的ProcessResult
	 * 参数1.rc：ResultCode{0：OK调用成功,-4：ConntectionLoss客户端和服务端连接已断开,-110：NodeExists指定节点已存在,-112：SessionExpired会话已过期}
	 * 参数2.path：接口调用时传入API的数据节点的节点路径参数值
	 * 参数3.ctx：接口调用时传入API的Context的值
	 * 参数4.name：实际在服务端创建的节点名
	 * @throws Exception
	 */
	@Test
	public void testCreateAsync() throws Exception {
		latch.await();
		zooKeeper.create("/testAsc", "干死黄旭东".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,(rc,path,ctx,name)->{
			System.out.println("创建的path结果：["+rc+","+path+","+ctx+","+name+"]");
		},"I am context");
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * Zookeeper只允许删除子节点，如果该节点有子节点的话 不允许删除
	 * @throws Exception
	 */
	@Test
	public void testDeleteSync() throws Exception{
		latch.await();
		zooKeeper.delete("/testAsc",1 , (rc,path,ctx)->{
			System.out.println("创建的path结果：["+rc+","+path+","+ctx+"]");
		}, "我是上下文");
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * 获取子节点列表
	 * @throws Exception
	 */
	@Test
	public void testGetChildNode() throws Exception{
		latch.await();
		List<String> nodeList
			= zooKeeper.getChildren("/", true);
		nodeList.forEach(System.out::println);
		//异步回调
	}
	
	/**
	 * 获取节点内的数据
	 * nt rc, String path, Object ctx, byte data[],
                Stat stat
	 * @throws Exception
	 */
	@Test
	public void testGetData() throws Exception{
		latch.await();
		zooKeeper.getData("/testAsc", event->{
			System.out.println("接收到事件" + event);
		}, (rc,path,ctx,data,stat)->{
			System.out.println("获取到的数据为 [rc:"+rc+",path:"+path+",ctx:"+ctx+",data:"+new String(data)+",stat:"+stat+"]");
		}, "我是上下文");
		Thread.sleep(Integer.MAX_VALUE);
	}
}
