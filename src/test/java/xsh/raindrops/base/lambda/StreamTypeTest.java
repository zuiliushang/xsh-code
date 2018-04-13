package xsh.raindrops.base.lambda;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * 
 * @author raindrops
 * @date 2018-04-10
 *
 */
public class StreamTypeTest {

	@Test
	public void intStreamTest(){
		System.out.println(Stream.of(1,4,6,2,3).mapToInt((i)->i).sum());
		//peek 惰性执行
		IntStream.of(1, 2, 3, 4)
        .filter(e -> e > 2)
        .peek(e -> System.out.println("Filtered value: " + e))
        .map(e -> e * e)
        .peek(e -> System.out.println("Mapped value: " + e))
        .sum();
	}

	@Test
	public void test1() {
		Stream
			.of(1,3,5,6,7,8,2,1,3,4)
			.parallel()
			.forEach(System.out::println);
	}
}
