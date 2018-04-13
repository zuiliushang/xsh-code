package xsh.raindrops.base.lambda;

import java.util.Spliterator;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamPipelineTest {
	
	@Test
	public void test01() {
		Spliterator<Integer> t = Stream.of(1,2,3).spliterator(),ls;
	}
	
}
