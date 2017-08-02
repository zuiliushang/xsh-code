package xsh.raindrops.nio.netty4;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 在 {@link HelloNettyServer}中用于实现服务器监听信息
 * @author Raindrops on 2017年6月12日
 *
 */
public class HelloHandler extends SimpleChannelHandler{
	/**
	 * 关闭连接，不管连接建立成功或者未成功 都能够触发
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("channelClosed : " + e);
		super.channelClosed(ctx, e);
	}
	/**
	 * 新连接
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("channelConnected : " + e);
		super.channelConnected(ctx, e);
	}
	/**
	 * 断开连接，建立的连接成功了关闭了通道的时候才会触发
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("channelDisconnected : " + e);
		super.channelDisconnected(ctx, e);
	}
	/**
	 * 捕获异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		System.out.println("exceptionCaught : " + e);
		super.exceptionCaught(ctx, e);
	}
	/**
	 * 接收消息 消息是以字节流保存在 {@link ChannelBuffer}
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		System.out.println("meaageReceived : " + e.getMessage());
		// ChannelBuffer 包装 字节流
		/*ChannelBuffer message = (ChannelBuffer) e.getMessage();
		String s = new String(message.array());
		System.out.println(s);*/
		// 回写数据 获取会话的 Channel
		//ChannelBuffer copiedBuffer = ChannelBuffers.copiedBuffer("hi 你好啊".getBytes("gbk"));
		ctx.getChannel().write("hello");
		//ctx.getChannel().close(); 关闭
		super.messageReceived(ctx, e);
	}
}
