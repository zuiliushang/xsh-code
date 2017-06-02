package xsh.raindrops.struct.hash;

/**
 * 基础哈希理论
 * @author Raindrops on 2017年5月17日
 */
public class BaseHash {

	/**
	 * 简单而且很快的计算出答案
	 * 但是对于tableSize很大 比如 10007
	 * 所有关键字至多 8个字符长
	 * 而ASCII最多是127 因此散列函数只能设置在 0 - 1016之间
	 * 1016 = 107 * 8;
	 * 不是均匀的分配
	 */
	public static int hash1( String key,int tableSize){
		int hashVal = 0;
		for (int i = 0; i < tableSize; i++) {
			hashVal += key.charAt(i);
		}
		return hashVal % tableSize;
	}
	
	/**
	 * 假设 key 至少有3个字符。
	 * 值 27 表示英文字母表外加一个空格的个数 26字母 + 空格数
	 * 729 是 27 * 3
	 * 能散列的字段有限制 <====>  tableSize
	 * 并且性能较为低下 不可能所有排列组合都是需求
	 */
	public static int hash2( String key,int tableSize){
		return (key.charAt(0) + 27 * key.charAt(1) +
				729 * key.charAt(2))%tableSize;
	}
	
	/**
	 * Pn(x)= anxn ＋an－１xn－１＋…＋a１x＋a０＝((…(((anx ＋an－１)x＋an－２)x+ an－3)…)x＋a1)x＋a０
	 */
	public static int hash3( String key , int tableSize ){
		int hashVal = 0;
		
		for (int i = 0; i < tableSize; i++) {
			hashVal = 37 * hashVal + key.charAt(i);
		}
		
		hashVal %= tableSize;
		if (hashVal < 0) {
			hashVal += tableSize;
		}
		return hashVal;
	}
	
	
	
	public static void main(String[] args){
		System.out.println(hash2("***",10007));
	}
	
}
