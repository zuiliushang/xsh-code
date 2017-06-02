package xsh.raindrops.base.lambda;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author Raindrops on 2017年5月12日
 *
 */
public class LambdaTest2 {
	
	@Test
	/**
	 * Arrays.sort的线程与调用sort的线程是同一线程吗？
	 * 答案：yes
	 */
	public void test01(){
		List<Integer> list = Arrays.asList(6,7,9,3,1,4,5);
		list.sort((a,b)->{
			System.out.println(Thread.currentThread().getName());
			return a.compareTo(b);
		});
	}
	
	@Test
	/**
	 * sorted()是不是只用于稳定流？
	 * 答案：yes
	 */
	public void test02(){
		List<Integer> list = Arrays.asList(6,7,9,3,1,4,5);
		list.stream().sorted(Comparator.comparing(Integer::intValue)
		).forEach((v)->{
			System.out.println(Thread.currentThread().getName());
			System.out.println(v);
		});
		list.parallelStream().sorted(Comparator.comparing(Integer::intValue)
				).forEach((v)->{
					System.out.println(Thread.currentThread().getName());
					System.out.println(v);
		});
		System.out.println(Thread.currentThread().getName());
	}
	
	@Test
	/**
     * 用FileNameFiler进行过滤
     */
	public void test03(){
		File f = new File("/");
		Arrays.asList(f.list((file,filename)->filename.endsWith(".md"))).forEach((v)->System.out.println(v));;
	}
	
}
