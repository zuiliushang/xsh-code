package xsh.raindrops.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author xusihan on 2017.08.30
 *
 */
public class CopyBytes {
	
	public static void main(String[] args) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream("src/test/resources/test.txt");
			out = new FileOutputStream("src/test/resources/test2.txt");
			int d;
			while ((d=in.read())!=-1) {
				out.write(d);
			}
		} finally {
			if (in!=null) {
				in.close();
			}
			if (out!=null) {
				out.close();
			}
		}
		
	}
}
