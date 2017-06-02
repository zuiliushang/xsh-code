package xsh.raindrops.distribution.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import xsh.raindrops.distribution.zookeeper.Constant;

/**
 * Curator 解决了很多ZK客户端非常底层的工作，包括连接重连
 * 反复注册watcher 和 {@link NodeExistsException} 异常等
 * @author Raindrops on 2017年5月31日
 *
 */
public class CreateCurator {
	
	@Test
	public  void test01() throws InterruptedException {
		/**
		 * 创建一个重试策略
		 * baseSleepTimeMs 初始sleep时间
		 * maxRetries 最大重试次数
		 * maxsleepMs 最大sleep时间
		 */
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = 
				CuratorFrameworkFactory.newClient(
						Constant.ZK_LINK_ADDR,
						5000,
						3000,
						retryPolicy);
		client.start();
		System.out.println("Client? start??");
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * 采用 Fluent 风格的API接口来创建一个 zookeeper 客户端
	 * @throws Exception 
	 */
	@Test
	public void testCreate() throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client =
				CuratorFrameworkFactory
					.builder()
					.connectString(Constant.ZK_LINK_ADDR)
					.sessionTimeoutMs(5000)
					.retryPolicy(retryPolicy)
					.build();
		client.start();
		
		String path="/curator";
		
		// 创建一个节点 初始化内容为空
		client.create().forPath(path);
		
		// 创建一个节点 初始化内容为 许思涵
		//client.create().forPath(path, "许思涵".getBytes());
		
		// 创建一个节点 临时的节点 内容为空
		//client.create().withMode( CreateMode.EPHEMERAL ).forPath(path);
	
		
		String path2 = "/curators/raindrops/zuiliushang/rotos";
		
		// 创建一个节点 临时节点 并且自动递归创建父节点
		// 创建一个节点 临时节点 并且自动递归创建父节点
		// 创建一个节点 临时节点 并且自动递归创建父节点
		// 创建一个节点 临时节点 并且自动递归创建父节点
		// 这个方法very有用 ye
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path2);
		
	}
	
	@Test
	public void testDel() throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client =
				CuratorFrameworkFactory
					.builder()
					.connectString(Constant.ZK_LINK_ADDR)
					.sessionTimeoutMs(5000)
					.retryPolicy(retryPolicy)
					.build();
		client.start();
		
		String path = "/curator";
		
		// 删除一个节点
		client.delete().forPath(path);
		
		// 递归删除节点
		client.delete().deletingChildrenIfNeeded().forPath(path);
	
		// 删除指定版本的节点
		client.delete().withVersion(1).forPath(path);
		
		/**
		 * guaranteed 是一个保障措施
		 * 只要客户端会话有效的话
		 * 那么 Curator 会在后台持续进行删除操作
		 * 直到节点删除成功
		 */
		// 删除一个节点，强制保证删除	
		client.delete().guaranteed().forPath(path);
		
	}
	
	@Test
	public void testRead() throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client =
				CuratorFrameworkFactory
					.builder()
					.connectString(Constant.ZK_LINK_ADDR)
					.sessionTimeoutMs(5000)
					.retryPolicy(retryPolicy)
					.build();
		client.start();
		
		String path = "/curator";
		
		// 查询节点
		client.getData().forPath(path);
		
		Stat stat = new Stat();
		
		// 读取一个节点的数据，并且或缺stat
		client.getData().storingStatIn(stat).forPath(path);
		
		
	}
	
	@Test
	public void testUpdate() throws Exception{
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = 
				CuratorFrameworkFactory
					.builder()
					.connectString(Constant.ZK_LINK_ADDR)
					.sessionTimeoutMs(5000)
					.retryPolicy(retryPolicy)
					.build();
		client.start();
		
		String path="/curator";
		
		client.setData().forPath(path);
		
		client.setData().withVersion(1).forPath(path);
		
	}
	
}
