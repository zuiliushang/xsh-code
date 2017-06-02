package xsh.raindrops.distribution.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 第一个ZK会话实例
 * @author Raindrops on 2017年5月24日
 * JavaAPI->创建连接->创建一个最基本的Zookeeper会话实例
 */
public class MyFirstZKSession implements Watcher{
	
	private static CountDownLatch latch = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent watchedEvent) {
		System.out.println("Receive watched event: " + watchedEvent);//接受watcher的时间
		if (KeeperState.SyncConnected == watchedEvent.getState()) {//接受到SyncConnected的命令 表示连接成功
			latch.countDown();//解除主程序在 CountDownLatch 等待阻塞
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ZooKeeper zooKeeper = new ZooKeeper(Constant.ZK_LINK_ADDR, 5000, new MyFirstZKSession());
		System.out.println(zooKeeper.getState());
		latch.await();
		System.out.println("Zookeeper session established");
	}
	
}
