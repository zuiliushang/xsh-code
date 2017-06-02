package xsh.raindrops.pattern;


/**
 * 单例
 * @author Raindrops on 2017年5月25日
 *
 */
public class LazySingleton {
	private LazySingleton() {}
	
	private static class LazySingletonHolder{
		static final LazySingleton singleton = new LazySingleton();
	}
	
	public static LazySingleton getInstance() {
		return LazySingletonHolder.singleton;
	}
	
}
