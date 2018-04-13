package xsh.raindrops.base.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeSet;
import java.util.stream.StreamSupport;

import org.junit.Test;

import com.google.common.collect.Iterators;

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
	
	@SuppressWarnings("unchecked")
	@Test
	public void testStreamsupv() {
		StreamSupport.stream(Spliterators.spliteratorUnknownSize(
				new HashSet(Arrays.asList(10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,52,1,2,3,4,5,6,7,8,9)).iterator()
				, Spliterator.NONNULL), true).forEach(System.out::println);
		// new HashSet(Arrays.asList(4,1,5,1291,3,2,4,5,4,3,2)).forEach(System.out::println);
	}
}
