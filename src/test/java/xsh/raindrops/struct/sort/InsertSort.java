package xsh.raindrops.struct.sort;

/**
 * 排序算法 - 插入排序
 * @author Raindrops on 2017年5月15日
 * <pre>
 * </pre>
 * 
 */
public class InsertSort {
	
	/**
	 * 直接插入排序 O(n2)
	 * @param arr
	 * @param low
	 * @param high
	 */
	public void insertSort(int[] arr,int low ,int high){
		for (int i = low+1 ; i < high; i++) {
			if (arr[i] < arr[i-1]) {//后一个比前一个小 那么要插入到前一个的位置
				int tmp = arr[i];
				arr[i] = arr[i-1];
				int j = i-1;//前一个的位置开始循环遍历到第一个 已排序好的
				for (; j >= low && tmp < arr[j]; j--) {
					arr[j+1] = arr[j];
				}
				arr[j+1] = tmp;
			}
		}
	}
	
	/**
	 * 关键点在于中间数据
	 * 折中插入排序 O(n2)
	 * @param arr
	 * @param low
	 * @param high
	 */
	public void insertSort2(int[] arr,int low,int high){
		for (int i = low + 1;i < high; i++) {
			int tmp = arr[i];
			int li = low + 1 ;
			int hi = i - 1;
			while (li <= hi) { //每次都从中间寻找 可以减少一半的查找量
				int mid = (li + hi)/2;
				if (arr[mid] < arr[i]) 
					hi = mid - 1;
				else
					li = mid + 1;
			}
			int j = i-1;
			for (; j >= low && tmp < arr[j]; j--) {
				arr[j+1] = arr[j];
			}
			arr[j+1] = tmp;
		}
	}
	
	/**
	 * 希尔排序
	 <pre>
	   先将一个待排序的数组分为几段，然后根据步长序列进行分离
	   分别对子集合进行插入排序
	 </pre>
	 * @param arr
	 * @param delta 步长序列
	 */
	public void shellSort(int[] arr,int low,int high,int[] delta){
		for (int k = 0; k < delta.length; k++) 
			shellInsert(arr,low,high,delta[k]);
	}  
	
	private void shellInsert(int[] arr,int low,int high,int deltaK){
		for (int i = low + deltaK; i < high; i++)//按照步长序列进行比较 
			if (arr[i] < arr[i-deltaK]) {// 后一个小于前一个时，将 arr[i] 插入有序表
				int tmp = arr[i]; //临时存放 arr[i]
				int j = i - deltaK;
				for (;j >= low && tmp < arr[j] ; j=j-deltaK) // 排序间隔数组
					arr[j+deltaK] = arr[j];
				arr[j+deltaK] = tmp;
			}
	}
	
	
	
	public static void main(String[] args){
		int[] arr =new int[]{5,1,-3,1,4,77,8,5,9,9,4,3,2,1,1,3,2,0,5,4,6,8,7};
		InsertSort sort = new InsertSort();
		sort.shellSort(arr, 0, arr.length, new int[]{11,5,2,1});
		for (int i = 0; i < arr.length; i++) {
			System.out.print("   " + arr[i]);
		} 
	}
}
