package xsh.raindrops.base.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.junit.Test;

public class StreamSupportTest {

	@Test
	public void testStreamSup() {
		// StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, characteristics), characteristics, parallel);
		List<String> sList = new ArrayList<String>();
		sList.addAll(Arrays.asList("rain","drops","split","jio","jio"));
		StreamSupport.stream(Spliterators.spliteratorUnknownSize(sList.iterator(), 
				Spliterator.ORDERED | Spliterator.DISTINCT), true)
					.forEach(System.out::println);
	}
	
}
