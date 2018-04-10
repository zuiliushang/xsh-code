package xsh.raindrops.concurrent;

import java.nio.ByteBuffer;

import org.junit.Test;

public class ByteBufferTest {

	/**
	 * flip 的作用 就是讲状态翻转
	 * 将 limit = position
	 * position = 0 
	 * mark = -1
	 * 比如我们从 0写到10 这时候需要取数据了
	 * 就flip
	 * 马上变成可以取
	 */
	@Test
	public void testFlip() {
		// 先分配一个64位的byteBuffer
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byte[] write = new byte[] {0,1,2,3,4,5};//6个
		byteBuffer.put(write);
		byteBuffer.flip();// 开始读 potition = 0 limit = 5 
		for (int i = 0;i < 6 ; i++) {
			byteBuffer.position();
			System.out.println(byteBuffer.get());
		}
		//再读一个 报错  因为 flip的时候 limit为 5了
		//不能读第六
		/*byteBuffer.position();
		System.out.println(byteBuffer.get());*/
		// 将byteBuffer limit 设置好
		byteBuffer.limit(7);
		byteBuffer.position();
		System.out.println(byteBuffer.get());
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10*1024*1024);
		byte[] obytes = new byte[10 * 1024 *1024];
		for (int i = 0 ; i < 10 * 1024 *1024 ; i++) {
			obytes[i] = (byte)i;
		}
		byteBuffer.put(obytes);//存入数据
		byteBuffer.position(10);//移动到位置10 position 为 10
		System.out.println(byteBuffer.get());//获取这个位置的数据
		byteBuffer.position(); // 移动到下一个位置 11 
		byteBuffer.mark();// 记录当前位置 11
		byteBuffer.position(15);// 强制移动到15
		System.out.println(byteBuffer.get());
		byteBuffer.reset();// 返回11
		System.out.println(byteBuffer.remaining());// 获取 10*1024*1024 - 11 的数量 使用的剩余量！
		System.out.println(byteBuffer.get());
	}
	
}
