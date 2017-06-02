package xsh.raindrops.struct.sort;

/**
 * 排序算法 - 选择排序算法
 * @author Raindrops on 2017年5月16日
 */
public class SelectSort {
	
	/**
	 * 简单选择排序
	 * @param arr
	 * @param row
	 * @param high
	 */
	public void simpleSelectSort(int[] arr,int row,int high){
		for (int i = row; i < high; i++) {
			for (int j = i+1; j < high; j++) {
				if (arr[i]>arr[j]) {
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
	}
	
	/**
	 * 堆排序
	 * @param arr
	 * @param row
	 * @param high
	 */
	public void heapAdjust(int[] arr,int low,int high){
		int tmp = arr[low];
		for (int i = 2*low; i <= high; i=i*2) { //沿着关键字较大的数进行筛选
			//i指向关键字较大的元素
			if (i<high && arr[i] <= arr[i+1]) {
				i++;
			}
			if (tmp >= arr[i]) {
				break;
			}
			//向下筛选
			arr[low] = arr[i];
			low = i;
		}
		arr[low] = tmp;
	}
	
	public void heapSort(int[] arr){
		int n = arr.length-1;
		for (int i = n/2; i >= 1; i--) {
			heapAdjust(arr, i, n);
		}
		for (int i = n; i > 1; i--) {
			int tmp = arr[1];
			arr[1] = arr[i];
			arr[i] = tmp;
			heapAdjust(arr, 1, i-1);
		}
	}
	
	public static void main(String[] args){
		int[] arr =new int[]{5,1,-3,1,4,77,8,5,9,9,4,3,2,1,1,3,2,0,5,4,6,8,7};
		SelectSort selectSort = new SelectSort();
		selectSort.heapSort(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.print("  " + arr[i]);
		}
	}
	
}
