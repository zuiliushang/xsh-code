package xsh.raindrops.base.lambda;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * 
 * @author Raindrops on 2017年5月12日
 *
 */
public class LambdaTest1 {
	
	@Test
	/**
	 * lambda 表达式
	 * lambda 排序 解决内部类问题
	 */
	public void test01(){
		// lambda没有参数 ()->{};
		// lambda参数可以推断(args)->{args++;}
		// 不需要为lambda表达式返回类型,可以从上下文推断出来(在某些分支中没有返回值是不合法的 比如if)
		List<Integer> list = Arrays.asList(1,2,3,6,2,1,3,5);
		// list.sort((n1,n2)->n1.compareTo(n2));
		list.sort(Comparator.comparing(Integer::intValue).reversed());
		list.forEach(System.out::println);
	}
	
	@Test
	/**
	 * lambda 表达式
	 * lambda 排序 解决Map排序
	 */
	public void test02(){
		Map<Integer, List<Integer>> maps = null;
		maps = Arrays.asList(1,3,4,5,1,2,4).stream().collect(Collectors.groupingBy(Integer::intValue));
		// 排序结果是 list
		maps.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList())
		.forEach(n->{
			n.getValue().forEach(System.out::println);
		});
		// 排序结果是map
		maps.entrySet().stream().sorted(Map.Entry.comparingByKey())
			.collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue))
			.forEach((k,v)->{
				System.out.println("k ->"+ k + ", v ->" +v );	
			});;
	}
	
	@Test
	/**
	 * lambda 表达式
	 * 函数式接口
	 * 这个接口必定是 FunctionalInterface 只有方法的接口
	 * 而且 必定只有一个 function 的接口
	 */
	public void test03(){
		BiFunction<Integer, Integer, Integer> fun = Integer::compare;
		
		Runnable run = ()->{
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		new Thread(run).run();
		
		Callable callable = ()->{
			TimeUnit.SECONDS.sleep(10);
			return null;
		};
		
		ExecutorService service = Executors.newFixedThreadPool(3);
		service.submit(callable);
		service.shutdown();
		
	}
	
	
	@Test
	/**
	 * 方法引用
	 * (1)对象::实例方法
	 * (2)类::静态方法
	 * (3)类::实例方法
	 * 如果有重载方法,编译器会试图从上下文中找到最匹配的一个方法
     * 方法引用不会单独存在,他会被转换为函数式接口的实例
	 */
	public void test04(){
		List<Integer> list = Arrays.asList(1,2,3,4,6,9,7,5);
		list.sort(Integer::compare); // 类::静态方法
		System.out.println(list);
		
		List<String> listS = Arrays.asList("1","2","3","4","6","9","7","5");
		listS.sort(String::compareToIgnoreCase); // 类::实例方法
		System.out.println(listS);
		
		new Thread(System.out::println).start();
		
	}
	
	
	@Test
	/**
	 * 变量作用域
	 * 在lambda中使用 this 
	 * 并不是调用 Runnable的this，而是这个类的this
	 * lambda表达式有闭包
	 */
	public void test05(){
		//此代码无法通过编译
		/*int j = 0;
		new Thread(()->{
			j++;
		});*/
		
		//巧妙的改变方式 通过引用
		int[] count = new int[1];
		new Thread(()->{
			count[0]++;
		});	
	}
	
}
