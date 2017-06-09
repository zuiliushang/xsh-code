package xsh.raindrops.serialize.kryo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 
 * 第一个Kryo
 * @author Raindrops on 2017年6月9日
 * 	Output对象继承至OutputStream，可以通过其将数据写入到它内部的bytebuffer
           速度快！见https://github.com/eishay/jvm-serializers/wiki/Staging-Results
	    支持互相引用，比如类A引用类B，类B引用类A，可以正确地反序列化。
	    支持多个引用同一个对象，比如多个类引用了同一个对象O，只会保存一份O的数据。
	    支持一些有用的注解，如@Tag，@Optional。
	    支持忽略指定的字段。
	    支持null
	    代码入侵少
	    代码比较简法（比起msgpack，少得多）

 */
public class MyFirstSerialize {
	public static void main(String[] args) {
		Kryo kryo = new Kryo();
		
		try {
			Output output = new Output(new FileOutputStream("E:\\k\\test.txt"));
			String strobj = "hello world";
			kryo.writeObject(output, strobj);
			output.close();
			
			
			Input input = new Input(new FileInputStream("E:\\\\k\\\\test.txt"));  
		    String someObject = kryo.readObject(input, String.class);  
		    System.out.println(someObject);  
		    input.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
