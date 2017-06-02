package xsh.raindrops.struct.sort;


/**
 * 排序算法 - 交换排序
 * @author Raindrops on 2017年5月16日
 */
public class SwapSort {
	
	/**
	 * 冒泡排序
	 * 1 5 4 2 3
	 * 1 4 2 3 5
	 * 1 2 3 4 5
	 * 1 2 3 4 5
	 * 1 2 3 4 5
	 */
	public void bubbleSort(int arr[],int row ,int high){
		for (int i = row; i <= high; i++) {
			for (int j = 1; j <= high-i; j++) {
				if (arr[j]<arr[j-1]) {
					int tmp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = tmp;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @param left
	 * @param right
	 */
	public void quickSort(int arr[],int left,int right){
		if (left < right) {
			int pa = partition(arr, left, right);
			quickSort(arr, left, pa - 1);
			quickSort(arr, pa + 1, right);
		}
	}
	
	/**
	   快速排序
	  .     
	  1 3 5 7 6
	  .
	  6 3 5 7 6
	  + + + + + (找到了)
	  6 3 5 7 1
	 * @param arr
	 * @param left
	 * @param right
	 * @return
	 */
	private int partition(int arr[],int left,int right){
		int pivot = arr[left]; //使用 arr[left] 作为枢轴
		while (left < right) {
			while (left < right && arr[right]<=pivot) 
				right--;
			arr[left] = arr[right];
			while (left < right && arr[left]>=pivot)
				left++;
			arr[right] = arr[left];
		}
		arr[left] = pivot;
		return left;
	}
	
	public static void main(String[] args){
		int[] arr =new int[]{5,1,-3,1,4,77,8,5,9,9,4,3,2,1,1,3,2,0,5,4,6,8,7};
		SwapSort sort = new SwapSort();
		//sort.quickSort(arr, 0, arr.length-1);
		sort.bubbleSort(arr, 0, arr.length-1);
		for (int i = 0; i < arr.length; i++) {
			System.out.print("   " + arr[i]);
		}
	}
	
}
