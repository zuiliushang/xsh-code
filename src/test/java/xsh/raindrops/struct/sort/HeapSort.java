package xsh.raindrops.struct.sort;

/**
 * 对N个互异项的随机排列组合进行堆排序所用的
 * 比较的平均次数为：2NlogN - O(N log logN)
 * @author Raindrops on 2017年5月19日
 */
public class HeapSort<T extends Comparable<? super T>> {
	/*public  <T extends Comparable<? super T>> void heapSort(T[] arr){
		for ( int i = arr.length/2 ; i >= 0 ;i--)
			perceDown(arr,i,arr.length);
		for (int i = arr.length - 1; i > 0; i--) {
			swapReferences(arr,0,i);
			perceDown(arr,0,i);
		}
		
	}
	
	private <T extends Comparable<? super T>> void swapReferences(T[] arr, int i, int n) {
		T tmp = arr[i];
		arr[i] = arr[n];
		arr[n] = tmp;
	}

	public <T extends Comparable<? super T>> void perceDown(T[] arr,int i,int n){
		int child;
		T tmp;
		for(tmp = arr[i];i*2 < n;i = child){
			child = i*2 ; 
			if (child != n-1 && arr[child].compareTo(arr[child+1]) < 0) 
				child++;
			if (tmp.compareTo(arr[child])<0)
				arr[i] = arr[child];
			else
				break;
				
		}
		arr[i] = tmp;
	}
	*/
	public static void main(String[] args){
		Integer[] i = new Integer[]{1,5,7,3,3,1,4,56,7};
		HeapSort heapSort = new HeapSort();
		heapSort.heapSort(i);
		for (int j = 0; j < i.length; j++) {
			System.out.print("   "+i[j]);
		}
	}
	
	
	/**
	  1.先创建一个最大/最小堆
	  2.每次循环都删除一个最大/最小的节点(根节点)
	  3.除去根节点
	  4.剩下的节点继续构成堆
	  5.只需要对根节点进行堆结构重构
	 */
	public void heapSort(T[] arr){
		for(int i = arr.length-1; i >= 0 ; i-- )
			precDown(arr,i,arr.length);
		for(int i = arr.length-1; i > 0 ; i-- ){
			swapReferences(arr,0,i);
			precDown(arr,0,i);
		}
	}

	private void swapReferences(T[] arr, int i, int length) {
		T tmp = arr[i];
		arr[i] = arr[length];
		arr[length] = tmp;
	}

	private void precDown(T[] arr, int i, int length) {
		int child;//用于代表孩子节点
		T tmp ;//得出最大根节点
		for (tmp = arr[i]; i*2 < length; i = child) {
			child = i*2;
			if (child!=length-1 && arr[child].compareTo(arr[child+1])<0)
				child++;
			if (tmp.compareTo(arr[child])<0)
				arr[i] = arr[child];
			else
				break;
		}
		arr[i] = tmp;
	}
	
}
