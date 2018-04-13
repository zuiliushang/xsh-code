package xsh.raindrops.base.lambda;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterator.OfDouble;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import org.junit.Test;

/**
 * 
 * @author xusihan
 * @date 2018-04-11
 *
 */
public class SpliteratorsTest {

	@Test
	public void testSplit() {
		StreamSupport.stream(Spliterators
				.spliterator(Arrays.asList(1,3,4,5,23,5,2,2), Spliterator.CONCURRENT|Spliterator.SIZED), false).forEach(System.out::println);;
	}
	
	/*@Test
	public void testSpliterators() {
		String v = "raindropsraindrops";
		StreamSupport.stream(Spliterators., parallel)
	}*/
	
	@Test
	public void testChar() {
		System.out.println(Character.isDigit('1'));
	}
	
	
	@Test
	public void testSpliterators1() {
		Spliterators.iterator(DoubleStream.of(1d,2d,3d,4d).spliterator());
			//.forEachRemaining(System.out::println);
		Spliterators.iterator(IntStream.of(1,2,3,4).spliterator());
			//.forEachRemaining(System.out::println);
		Spliterators.iterator(LongStream.of(1,2,3,4).spliterator());
			//.forEachRemaining(System.out::println);
		Spliterators.iterator(LongStream.of(1,2,3,4).spliterator());
			//.forEachRemaining(System.out::println);
		Spliterators.iterator(Arrays.asList(1,2,3).spliterator())
			.forEachRemaining(System.out::println);
		Spliterators.spliterator(Arrays.asList("123","321","123","321")
				, Spliterator.IMMUTABLE).forEachRemaining(System.out::println);
		Spliterators.spliterator(Arrays.asList(1,2,3,4,5,6,7,6,5,4,3,2,1,2).iterator(), 5, Spliterator.IMMUTABLE)
			.forEachRemaining(System.out::println);
		Spliterators.spliteratorUnknownSize(Arrays.asList(1,2,3,4,5,6,7,6,5,4,3,2,1,2).iterator(), Spliterator.IMMUTABLE)
			.forEachRemaining(System.out::println);
	}
	
}
	