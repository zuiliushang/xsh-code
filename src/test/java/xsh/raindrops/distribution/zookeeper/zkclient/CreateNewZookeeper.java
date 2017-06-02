package xsh.raindrops.distribution.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

import xsh.raindrops.distribution.zookeeper.Constant;

public class CreateNewZookeeper {
	public static void main(String[] args) {
		/**
		 * zkClient 通过内部包装，将创建ZK连接的异步过程同步化
		 */
		ZkClient zkClient = new ZkClient(Constant.ZK_LINK_ADDR, 5000);
		System.out.println("Zookeeper session established");
	}
}
