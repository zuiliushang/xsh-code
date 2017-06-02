package xsh.raindrops.lock.util;
import java.lang.reflect.Field;

import sun.misc.Unsafe;
/**
 * 
 * @author Raindrops on 2017年5月25日
 *
 */
public class UnsafeUtil {
	public static Unsafe getUnfase() {
		Unsafe unsafe;
		try {
			Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (sun.misc.Unsafe) field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException("get unsafe Instance failed");
		}
		return unsafe;
	}
	
	public static long getFieldOffset(Class clazz, String fieldName) {
		try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return getUnfase().objectFieldOffset(field);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("unable to get Field");
        }
	}
	
}
