package xsh.raindrops.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * 和普通ArrayList差不多 只不过用了ReentrantLock来操作 保证线程安全
 * @author Raindrops
 * @date 2018-04-17
 *
 */
public class CopyOnWriterListTest {

	private static CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
	
	private static ArrayList<Integer> copyOnWriteArrayList1 = new ArrayList<>();
	
	public static void main(String[] args) {
		List<Integer> baseList  = Arrays.asList(1,2,3,4,5,6,7,8,9,0,1,2,4,34,23,51,2,1,4,15,32,5,35,345,34,534,5,345,2,4,31,41,43,235,43543,6,436,24,1);
		copyOnWriteArrayList.addAll(baseList);
		copyOnWriteArrayList.forEach(t->{
			if(t == 23) {
				copyOnWriteArrayList.remove(14);
			}
			System.out.println(t);
		});
	}
	
}
