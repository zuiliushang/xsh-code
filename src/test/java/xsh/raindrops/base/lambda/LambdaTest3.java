package xsh.raindrops.base.lambda;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import net.bytebuddy.asm.Advice.Return;

/**
 * 
 * @author Raindrops on 2017年5月12日
 *
 */
public class LambdaTest3 {
	
	private static List<String> list;
	
	@Before
	public void init(){
		list = Arrays.asList("raindrops","zuiliushang","rotos");
	}
	
	@Test
	public void test01(){
		list.sort(comparing(String::length)
				.thenComparing(String::toString)
				.thenComparingInt(str->str.indexOf("3")));
		list.forEach(System.out::println);
		
		Long count1 = list.stream().filter(u->u.length()>5).count();
		Long count2 = list.parallelStream().filter(u->u.length()>10).count();
		System.out.println(count1);
		System.out.println(count2);
		
		//在执行stream,filter时,并没有执行,stream操作符是延迟执行的
		Stream<String> stream = list.stream().filter(u->u.length()>5);
		System.out.println("=-=-=-=-=---====--=-=-=-=");
		stream.forEach(System.out::println);
		System.out.println(list);
	}
	
	@Test
	/**
	 * 创建Stream
	 */
	public void test02(){
		Stream.of(new String[]{"1"});
		final IntStream intStream = Arrays.stream(new int[]{1,2,3,14,5});
		infoStream(intStream);
		Stream.empty();
		Stream.of(new String[]{"a","c","d","w","q"})
			.map(String::toUpperCase)
			.peek(System.out::println)
			.collect(Collectors.toList());
		
	}
	
	@Test
	/**
	 * filter,map,flatMap
	 */
	public void test03(){
		final List<String> rsList = Arrays.asList("123","456","abc");
		final Stream<Stream<String>> mapStream = 
				rsList
					.stream()
					.map(LambdaTest3::characterStream);
		mapStream.forEach(s->{
			s.forEach(System.out::println);
		});
		final Stream<String> flatMapStream = 
				rsList
					.stream()
					.flatMap(LambdaTest3::characterStream);
		flatMapStream.forEach(System.out::println);
	}
	
	@Test
	/**
	 * limit,skip,contact 执行流操作
	 */
	public void test04(){
		//剪切前100个流
		final Stream<Double> limit = Stream.generate(Math::random).limit(3);
		limit.forEach(System.out::println);
		//12
		Stream.of(1,2,3,4,5,6,7,8).limit(2).forEach(System.out::print);
		//裁剪流
		//345678
		Stream.of(1,2,3,4,5,6,7,8).skip(2).forEach(System.out::print);
		System.out.println("");
        System.out.println("===");
        //连接流
        Stream.concat(Stream.of(2), Stream.of(2)).forEach(System.out::print);
	}
	
	@Test
	/**
	 * 有状态的转换
	 */
	public void test05(){
		final Stream<String> stStream = Stream.of("1","2","3","6","5","4");
		stStream
			.peek(System.out::print)
			.distinct()
			.peek(System.out::print)
			.count();
		System.out.println("============");
		
		final Stream<String> stream = Stream.of("1j1jana","91jsadnf","wjef921");
		// stream.sorted方法返回一个新的已排序的流
		stream.distinct().sorted(comparing(String::length).thenComparing(arg->arg.indexOf("j")).reversed())
		.peek(System.out::println)
		.count();
	}
	
	@Test
	/**
	 * 聚合方法：将流聚合成一个值？
	 * 集合之后流关闭，不能再次使用
	 * 1.count 返回数量
	 * 2.max 返回一个 Optional对象
	 * 3.findFirst 查找第一个
	 * 4.findAny 用.parallelStream 返回的是任意一个
	 * 5.anyMatch,noneMatch,allMatch接收一个Predict的function 返回一个boolean
	 */
	public void test06(){
		final Stream<String> maxStream = Stream.of("1","3456","123","12");
		final Optional<String> max = maxStream
				.max(comparing(String::length));
		if (max.isPresent()) {
			System.out.println(max.get());
		}
		final Stream<String> oneStream = Stream.of("1","3456","123","12");
		final Optional<String> one = oneStream
				.findFirst();
		if (one.isPresent()) {
			System.out.println(one.get());
		}
		final Stream<String> anyStream = Stream.of("1","3456","123","12");
		final Optional<String> any = anyStream
				.findAny();
		if (any.isPresent()) {
			System.out.println(any.get());
		}
	}
	
	@Test
	/**
	 * 使用 Optional
	 */
	public void test07(){
		Optional<String>	optional = Optional.of("12345");
		//ifPresent 接收一个Consumer 无返回值
		optional.ifPresent(System.out::println);
		//.map接收一个Function<T,R>,有返回值，可能是 null的 Optional
		final Optional<String> nullOp = optional.map(arg -> null);
		if (!nullOp.isPresent()) {
			System.out.println("null value");
		}
		
		// Optional的用处
		// 优雅处理 set if empty
		final Optional<String> empty = Optional.empty();
		final String s = empty.orElseGet(()->{
			System.out.println("or Else get");
			return "raindrops";
		});
		System.out.println(s);
		
		String content = empty.orElse("zuiliushang");
		System.out.println(content);
		empty.orElseThrow(()-> new RuntimeException("我是控控的"));
		String abc =  empty.orElse(null);
		System.out.println(abc);
	}
	
	@Test
	/**
	 * 聚合操作reduce
	 */
	public void test08(){
		final Integer reduce1 = Stream.of(1,2,3,4,5,6,7,8).reduce((x,y)-> x+y).get();
		Assert.isTrue(reduce1==36);
		final Integer reduce2 = Stream.of(1,2,3,4,5,6,7,8).reduce(3, ((x,y)->x+y));
		Assert.isTrue(reduce2==39);
		final Integer reduce3 = Stream.of("aa","a","f")
				// 第二个参数为统计规则
				.reduce(0,(total,word)->total + word.length(),(total1,total2)->total1+total2);
		Assert.isTrue(reduce3==4);
		final Integer reduce4 = Arrays.asList("1","2","3").stream()
				.reduce(0,(total,word)-> total + word.length(), (total1, total2) -> total1 + total2);
		Assert.isTrue(reduce4==3);
	}
	
	
	@Test
	/**
	 * 收集结果到[]和List中
	 */
	public void test09(){
		// 收集到String[] 中
		final String[] strings = Stream.of("0","1","2").toArray(String[]::new);
		for (String string : strings) {
			System.out.println(string);
		}
		System.out.println("=================");
		
		final HashSet<Object> hashSet = 
				Stream.of("1","2","3","4").collect(HashSet::new, HashSet::add, HashSet::addAll);
		for (Object object : hashSet) {
			System.out.println(object);
		}
		
		final Set<String> hashSet2 = 
				Stream.of("1","2","3","4").collect(Collectors.toSet());
		for (String string : hashSet2) {
			System.out.println(string);
		}
		System.out.println("=================");
		//forEach  forEach和forEachOrder是终止操作,
		//结束后你就不能操作这个流了(peek不会立即执行,可以对每个流执行某操作)
		Stream.of("1","2","3","4","7","6","5").forEach(System.out::print);
		System.out.println("\n=================");
		Stream.of("1","2","3","4","7","6","5").forEachOrdered(System.out::print);
		System.out.println("\n=================");
		Stream.of("a","b","c","d","e","f","1").peek(String::toUpperCase).forEach(System.out::print);
		System.out.println("\n=================");
		Stream.of("a","b","c","d","e","f","1").map(String::toUpperCase).forEach(System.out::print);
	}
	
	@Test
	/**
	 * 收集到 map 中
	 */
	public void test10(){
		
	}
	
	 /**
     * 使用peek打印stream信息
     *
     * @param stream
     */
    private static void infoStream(IntStream stream) {
//        stream.peek(System.out::print);
        stream.peek(System.out::println).count();
    }
    
    private static Stream<String> characterStream(String s) {
        List<String> result = new ArrayList<>();
        Arrays.stream(s.split("")).forEach(result::add);
        return result.stream();
    }
    
    @Test
    public void sd() {
    	
    	/*String b = new String();
    	Arrays.asList("haha","hsadf","sfas")
    	.forEach(u->{b = u;});
    	System.out.println(b);;*/
    	
    	String[] a = new String[1];
    	Arrays.asList("haha","hsadf","sfas")
    	.forEach(u->{a[0] = u;});
    	System.out.println(a[0]);;
    }
    
    @Test
    public void testmAP() {
    	List<people> pList = Arrays.asList(new people(1,0.0f, "rara"),new people(2, 1f,"rara"),new people(4,2f, "rara"),new people(-1,3.0f, "rara"));
    	List<Integer> integers = pList.stream().filter(u->{return !u.getMon().equals(0);}).map(people::getAge).collect(Collectors.toList());
    	integers.forEach(System.out::println);
    }
    
    
    class people{
    	int age;
    	Float mon;
    	String name;
		public people(int age,Float mon, String name) {
			super();
			this.mon = mon;
			this.age = age;
			this.name = name;
		}
		
		public Float getMon() {
			return mon;
		}

		public void setMon(Float mon) {
			this.mon = mon;
		}

		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    	
    }
    
    
    
    
    /*
    
    id | select_type | table | type | possible_keys | key | key_len | ref  | rows | Extra |
    1  | SIMPLE      | event | ALL  | NULL          | NULL|   NULL  | NULL | 13   |       |  
    序列号  普通查询		所引用的表。	
    	联合查询
    	子查询之
    	类的复杂
    	查询。
    
    system > const > eq_ref > ref >
	fulltext > ref_or_null > index_merge 
	> unique_subquery > index_subquery > 
	range > index > ALL
    
    
    
    */
}
