package xsh.raindrops.fio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author xusihan on 2017.08.30
 */
public class PathMain {
	
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("G:\\project\\github\\xsh-code");
		System.out.format("toString: %s%n", path.toString());
		System.out.format("getFileName: %s%n", path.getFileName());
		System.out.format("getName(0): %s%n", path.getName(0));
		System.out.format("getNameCount: %d%n", path.getNameCount());
		System.out.format("subpath(0,2): %s%n", path.subpath(0,2));
		System.out.format("getParent: %s%n", path.getParent());
		System.out.format("getRoot: %s%n", path.getRoot());
		System.out.println("===================================");
		//转化成一个路径
		System.out.format("%s%n", path.toUri());
		System.out.println("===================================");
		//现有文件的真实路径
		Path fp = path.toRealPath();
		System.out.format("toString: %s%n", fp.toString());
		System.out.format("getFileName: %s%n", fp.getFileName());
		System.out.format("getName(0): %s%n", fp.getName(0));
		System.out.format("getNameCount: %d%n", fp.getNameCount());
		System.out.format("subpath(0,2): %s%n", fp.subpath(0,2));
		System.out.format("getParent: %s%n", fp.getParent());
		System.out.format("getRoot: %s%n", fp.getRoot());
		
	}
	
}
