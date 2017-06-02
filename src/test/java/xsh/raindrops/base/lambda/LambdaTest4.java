package xsh.raindrops.base.lambda;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.Test;
import com.google.common.collect.Lists;

/**
 * 
 * @author Raindrops on 2017年5月15日
 *
 */
public class LambdaTest4 {
	
	@Test
	/**
	 * 返回值是 boolean 可以用 Predicate 或  Function<User,Boolean> 来接收
	 */
	public void test01(){
		//返回值是boolean 可以用Predicate 或  Function<User, Boolean> 接收
       /* final Function<User, Boolean> isHigh = User::isHigh;
        Predicate<User> us = User::isHigh;
        System.out.println(isHigh.apply(new User(5)));
        System.out.println(us.test(new User(5)));

        final IntPredicate intPredicate = (int i) -> i % 2 == 0;
        System.out.println(intPredicate.test(100000));

        int i = 3;
        final IntSupplier doubleSupplier = () -> i;
//        i = 4;


        Lists.newArrayList(1, 3, 4, 5).sort(comparing(Integer::toHexString).reversed().thenComparing(Integer::byteValue));

        Map<String, List<Integer>> collect =
                Lists.newArrayList(1, 3, 4, 5)
                        .stream()
                        .collect(groupingBy(Integer::toHexString));


        final Stream<int[]> limit = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]}).limit(3);
        limit.forEach(k->{
            System.out.println(k[0]+"...."+k[1]);
        });

        final Predicate<User> isHigh1 = User::isHigh;
        isHigh1.and(User::isHigh).or(User::isHigh);*/
	}
	
}

class User{
	private String name;
	private String info;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
