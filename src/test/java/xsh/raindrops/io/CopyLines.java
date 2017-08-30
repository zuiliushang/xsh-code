package xsh.raindrops.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xusihan on 2017.08.30
 */
public class CopyLines {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = null;
		PrintWriter writer = null;
		
		try {
			reader = new BufferedReader(new FileReader("src/test/resources/test.txt"));
			writer = new PrintWriter(new FileWriter("src/test/resources/test2.txt"));
			
			String l;
			while((l=reader.readLine())!=null) {
				//writer.write(l);
				writer.println(l);
			}
		}finally {
			if (reader!=null) {
				reader.close();
			}
			if (writer!=null) {
				writer.close();
			}
		}
	}
}
