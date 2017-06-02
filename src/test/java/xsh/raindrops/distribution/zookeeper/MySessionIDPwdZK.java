package xsh.raindrops.distribution.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class MySessionIDPwdZK implements Watcher{

	private static CountDownLatch latch = new CountDownLatch(1);
	
	@Override
	public void process(WatchedEvent paramWatchedEvent) {
		System.out.println("Receive watched event : " + paramWatchedEvent);
		if (KeeperState.SyncConnected==paramWatchedEvent.getState()) {
			latch.countDown();
		}
	}

	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper
			= new ZooKeeper(Constant.ZK_LINK_ADDR, 5000, new MySessionIDPwdZK());
		long sessionId = zooKeeper.getSessionId();
		byte[] passwd = zooKeeper.getSessionPasswd();
		
		zooKeeper =
			new ZooKeeper(Constant.ZK_LINK_ADDR, 5000, new MySessionIDPwdZK(), 1l, "raindrops".getBytes());
		Thread.sleep( Integer.MAX_VALUE );
		zooKeeper = 
				new ZooKeeper(Constant.ZK_LINK_ADDR, 5000, new MySessionIDPwdZK(), sessionId, passwd);
	}
	
}
