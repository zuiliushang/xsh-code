package xsh.raindrops.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * 默认情况下，扫描器使用空白分隔令牌。
 * (空格字符包括空格、制表符和行终止符。对于完整的列表，请参阅字符的文档。
 * 为了了解扫描如何工作，让我们看一下ScanXan，它是在xanadu中读取单个单词的程序。txt打印出来，每行一个。
 * @author xusihan on 2017.08.30
 */
public class ScanXan {

	public static void main(String[] args) throws IOException {
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader("src/test/resources/test.txt")));
			s.useDelimiter("A");
			while (s.hasNext()) {
				System.out.println(s.next());
			}
		}finally {
			if (s!=null) {
				s.close();
			}
		}
	}
	
}
