package xsh.raindrops.nio.netty4;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import xsh.raindrops.nio.Constants;

/**
 * netty 3.x 4.x 客户端
 * @author Raindrops on 2017年6月12日
 */
public class HelloNettyClient {
	public static void main(String[] args) {
		//1.新建一个服务类
		ClientBootstrap bootstrap = new ClientBootstrap();
		//2.新建2个线程池
		ExecutorService bossExecutor = Executors.newCachedThreadPool();
		ExecutorService workerExecutor = Executors.newCachedThreadPool();
		//3.socket工厂
		bootstrap.setFactory(new NioClientSocketChannelFactory(bossExecutor, workerExecutor));
		//4.设置管道
		bootstrap.setPipelineFactory(()->{
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("decoder", new StringDecoder());
			pipeline.addLast("encoder", new StringEncoder());
			pipeline.addLast("helloClientHandler", new HelloClientHandler());
			return pipeline;
		});
		//5.连接服务器
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress(Constants.NETTY_ADDR,Constants.NETTY_PORT));
		Channel channel = connect.getChannel();
		System.out.println("client start");
		//6.开始写数据
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("输入");
			channel.write(scanner.next());
		}
	}
}
