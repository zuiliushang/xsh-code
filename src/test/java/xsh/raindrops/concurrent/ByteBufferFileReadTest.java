package xsh.raindrops.concurrent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class ByteBufferFileReadTest {
	
	/**
	 * 简单的一句话写
	 * @throws IOException 
	 */
	@Test
	public void writeFileSimpleTest() throws IOException {
		byte[] src = "A".getBytes();
		ByteBuffer byteBuffer = ByteBuffer.wrap(src);
		FileChannel channel = FileChannel.open(Paths.get("G://ByteBufferSimple.txt"), StandardOpenOption.WRITE);
		channel.write(byteBuffer);
		System.out.println(src);
	}
	
	@Test
	public void readFileSimpleTest() throws IOException {
		FileChannel channel = FileChannel.open(Paths.get("G://ByteBufferSimple.txt"), StandardOpenOption.READ);
		ByteBuffer byteBuffer = ByteBuffer.allocate(1);// 一次读一位
		int i = 0;
		byte[] bs = new byte[(int) channel.size()];
		for(;i<channel.size();i++) {
			channel.read(byteBuffer);
			bs[i] = byteBuffer.get(0);
			byteBuffer.clear();//
		}
		System.out.println(new String(bs));
		System.out.println(bs);
	}
	
}
