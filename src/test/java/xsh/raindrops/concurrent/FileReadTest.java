package xsh.raindrops.concurrent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class FileReadTest {

	@Test
	public void write100MB() throws Exception {
		FileChannel channel = FileChannel.open(Paths.get("G:\\1MB.txt"), StandardOpenOption.WRITE);
		
 	}
	
	public static void main(String[] args) throws IOException {
		AsynchronousFileChannel channel = AsynchronousFileChannel.open((new File("")).toPath(), StandardOpenOption.READ);
	}
	
}
