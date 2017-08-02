package xsh.raindrops.nio.netty5;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty5.x服务端
 * @author Raindrops on 2017年6月12日
 */
public class HelloNettyServer {
	public static void main(String[] args) {
		//1.创建一个服务类
		ServerBootstrap bootstrap = new ServerBootstrap();
		//2.BOSS 和 WORKER
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			//3.设置线程池
			bootstrap.group(boss,worker);
			//4.设置Socket工厂
			bootstrap.channel(NioServerSocketChannel.class);
			//5.设置管道工厂
			bootstrap.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new StringEncoder());
					ch.pipeline().addLast(new HelloServerHandler());
				}
			});
			// 额外的设置参数 TCP参数
			bootstrap.option(ChannelOption.SO_BACKLOG, 2048);//serverSocketchannel的设置 连接缓冲池的大小，accept最多2048个链接
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//socketchannel的设置，（默认2小时）true 维持链接的活跃，清除死链接
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);//socketchannel 设置，关闭延迟发送
			
			// 6.绑定端口
			ChannelFuture future = bootstrap.bind(10101);
						
			System.out.println("start");
						
						// 7.等待服务端关闭
			future.channel().closeFuture().sync();// 负责监听端口的 channel
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
