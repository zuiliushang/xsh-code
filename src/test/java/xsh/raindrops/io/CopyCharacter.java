package xsh.raindrops.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * JAVA 平台存储约定用Unicode编码。字符流I / O自动将这种内部格式转换为本地字符集。
 * 在西部地区，本地字符集通常是一套8位的ASCII码。
 * 
 * 对于大多数应用。Character I/O比byte stream的IO更加简单。输入和输出的完成Character I/O会通过本地字符集翻译成
 * 流。使用字符流代替字节流的程序会自动适应本地字符集，并为国际化做好准备——这一切都无需程序员的额外努力。
 * 
 * @author xusihan on 2017.08.30
 */
public class CopyCharacter {
	
	public static void main(String[] args) throws IOException {
		
		FileReader reader = null;
		FileWriter writer = null;
		try {
			reader = new FileReader("src/test/resources/test.txt");
			writer = new FileWriter("src/test/resources/test2.txt");
			int b;
			while ((b = reader.read())!=-1) {
				writer.write(b);
			}
		} finally {
			if (reader!=null) {
				reader.close();
			}
			if (writer!=null) {
				writer.close();
			}
		}
		
	}
	
}
