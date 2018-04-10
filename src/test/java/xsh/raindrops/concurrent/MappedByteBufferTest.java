package xsh.raindrops.concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class MappedByteBufferTest {

	@Test
	public void testOutOfMemory() throws InterruptedException {
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[1*1024*1024*1024] );//allocate(500*1024*1024*1024); 不会直接分配这么多 而是预分配？？
		byteBuffer.put(new byte[1*1024*1024*1024]);
		//2G 我的电脑就溢出了
		Thread.sleep(100000);
		System.out.println("haha");
	}
	
	@Test
	public void write1Gdata() throws IOException {
		Writer writer = new FileWriter("G://ByteBufferSimple1G.txt");
		for (int i = 0 ; i < 1*1024*1024*512 ; i ++ ) {
			writer.write("A"+i%10);
		}
		writer.flush();
		writer.close();
	}
	
	
	@Test
	public void read1Gdata() throws IOException {
		long start = System.currentTimeMillis();
		FileChannel fileChannel = FileChannel.open(Paths.get("G://ByteBufferSimple1G.txt"), StandardOpenOption.READ);
		ByteBuffer byteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
		for (int i = 0;i < byteBuffer.limit();i++) {
			byteBuffer.get();
		}
		System.out.println(System.currentTimeMillis()-start);
	}
	
	
	@Test
	public void read1GdataFormat() throws IOException {
		long start = System.currentTimeMillis();
		InputStream inputStream = new FileInputStream(new File("G://ByteBufferSimple1G.txt"));
		int b = 0;
		byte[] buf = new byte[1024];
		while((b = inputStream.read())!=-1) {
		}
		inputStream.close();
		System.out.println(System.currentTimeMillis()-start);
	}
	
	@Test
	public void write2GData() throws IOException {
		Writer writer = new FileWriter("G://ByteBufferSimple5G.txt");
		for (int i = 0 ; i < 1*1024*1024*1024 ; i ++ ) {
			writer.write("A");
		}
		for (int i = 0 ; i < 1*1024*1024*1024 ; i ++ ) {
			writer.write("A");
		}
		writer.flush();
		writer.close();
	}
	
	
	@Test
	public void read2GDataException() throws IOException {
		byte[] data = Files.readAllBytes(Paths.get("G://ByteBufferSimple5G.txt"));
	}

	@Test
	public void read2GDataByRandomAccess() throws IOException {
		@SuppressWarnings("resource")
		RandomAccessFile randomAccessFile = new RandomAccessFile("G://ByteBufferSimple5G.txt", "r");
		FileChannel fileChannel = randomAccessFile.getChannel();
		ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length());
		System.out.println(byteBuffer.limit());
	}
	
	@Test
	public void read2GDataMapped() throws IOException {
		FileChannel fileChannel =FileChannel.open(Paths.get("G://ByteBufferSimple5G.txt"), StandardOpenOption.READ);
		long size = fileChannel.size();
		int[] row = new int[] {(int) (fileChannel.size()/Integer.MAX_VALUE)};
		Stream.iterate(0, cut->(cut));
		System.out.println(size);
		int mapSize = 1024 * 1024 * 1024;
		MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, mapSize);
	}
	
}
