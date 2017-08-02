package xsh.raindrops.nio.netty4;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import xsh.raindrops.nio.Constants;

/**
 * Netty3.x 4.x 写法
 * @author Raindrops on 2017年6月12日
 *
 */
public class HelloNettyServer {
	public static void main(String[] args) {
		// 1.新建一个 netty 的服务类
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 2.创建两个线程池
		ExecutorService bossExecutor = Executors.newCachedThreadPool();//BOSS线程池负责监听窗口
		ExecutorService workerExecutor = Executors.newCachedThreadPool();//Worker线程池负责读写信息
		// 3.创建一个 NIO的工厂类
		bootstrap.setFactory(new NioServerSocketChannelFactory(bossExecutor, workerExecutor));
		// 4.设置管道的工厂
		bootstrap.setPipelineFactory(()->{
			/*
			 * pipeline 管道分为上行和下行管道
			 * 客户端给服务器 上行数据 经过 StringDecoder 方法
			 * 服务器给客户端 下行数据 经过 StringEncoder 方法
			 */
			ChannelPipeline pipeline = Channels.pipeline();//获取通道
			//自动转化ChannelBuffer
			pipeline.addLast("decoder", new StringDecoder());
			pipeline.addLast("endoder", new StringEncoder());
			//新建一个Handler继承 SimpleChannelHandler来接收管道信息
			pipeline.addLast("helloHandler", new HelloHandler());
			return pipeline;//放行管道
		});
		// 5.绑定一个端口
		bootstrap.bind(new InetSocketAddress(Constants.NETTY_PORT));
		System.out.println("server start");
	}
}
