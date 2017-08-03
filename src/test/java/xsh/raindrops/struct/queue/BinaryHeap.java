package xsh.raindrops.struct.queue;

import java.util.ArrayList;
import java.util.List;

import xsh.raindrops.struct.tree.UnderflowException;

/**
 * 二叉堆
 * 完全二叉树
 * 所以 一个堆结构由 一个数组 和代表数组上堆的节点数
  ---------------------
  | |A|B|C|D|E|F| | | |
  ---------------------
  				 A
  			  B     C 
            D   E F   
  对于父节点位置 I 左儿子一定是 2I 右儿子一定是2I+1
   比如  A - 1 那么 B - 2 C - 3
      B - 2 那么 D - 4 E - 5
 *
 * @author Raindrops on 2017年5月18日
 */
public class BinaryHeap <T extends Comparable<? super T>> {
	
	private static final int DEFAULT_CAPACITY = 10;
	/**
	 * 记录二叉树的元素个数
	 */
	private int currentSize;
	/**
	 * 因为完全二叉树排放很有规律
	 * 所以不需要用链状结构来进行
	 * 存放，这里使用数据即可
	 */
	private T[] array;
	
	public BinaryHeap(){
		this(DEFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public BinaryHeap(int size){
		currentSize = 0;
		array = (T[]) new Comparable[size];
	}
	
	@SuppressWarnings("unchecked")
	public BinaryHeap(T[] rows){
		currentSize = rows.length;
		this.array = (T[]) new Comparable[(currentSize + 2) * 11 / 10];
		int i = 1;// 节点位置为在1 0不存放节点
		for (T t : rows) {
			this.array[i++] = t;
		}
		buildHeap();
	}
	
	/**
	 * 先给树最后一个节点尝试添加一个节点
	 * 看看是否破坏了堆结构
	 * 如果破坏了 将此节点进行上滤？
	 */
	public void insert(T t){
		if (currentSize == array.length - 1) {//长度-1没错 第一个 0 是不存放的
			enlargeArray( array.length*2 + 1);
		}
		// percolate up 上滤
		int hole = ++currentSize;
		for ( ; hole > 1 && t.compareTo(array[hole/2])<0; hole/=2) {
			array[hole] = array[hole/2];
		}
		array[hole] = t;
	}
	
	public T findMin(){
		if (array==null) {
			return null;
		}
		return array[1];
	}
	
	public T deleteMin(){
		// 删除最小可以看做是 把最后一个节点插入第一个节点 再进行下滤
		if (isEmpty()) {
			throw new UnderflowException();
		}
		T t = findMin();
		array[1] = array[currentSize--];
		percolateDown(1);
		return t;
	}
	
	public boolean isEmpty(){
		return currentSize==0;
	}
	
	public void makeEmpty(){
		currentSize=0;
		array = null;
	}
	
	//根据数组来初始化堆
	private void buildHeap(){
		for(int i = currentSize/2;i>0;i--){//界定取为 [0 - 数组/2]
			percolateDown(i);
		}
	}
	// 下滤 hole为根节点进行过滤 从根开始过滤 减少过滤重复率
	private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];
        //这个循环保证了所有的节点在修改的时候 调整可能被破坏的路径
        for(; hole * 2 <= currentSize; hole = child) {
        	//排序第一个左节点
            child = hole * 2;
            //如果这个左节点不是最后一个节点
            //比较 左边还是右边的大
            // 左边的大的话 就要右边的(小的)
            if (child != currentSize && array[child].compareTo(array[child + 1]) > 0) {
                child++;
            }
            // 这个小的和原来的数据比较
            // 取小的来
            if (tmp.compareTo(array[child]) > 0) {
                array[hole] = array[child];
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }
	
	/**
	 * 重组array
	 * @param newSize
	 */
	private void enlargeArray(int newSize) {
        List<T> newArray = new ArrayList<T>(newSize);
        for(int i = 0; i < currentSize; i++)
            newArray.add(array[i]);
        array = (T[]) newArray.toArray();
    }
	
	public static void main(String[] args){
		/*BinaryHeap<Integer> heap = new BinaryHeap<>(new Integer[]{100,30,20,80,10,90,60,150,40,130,110,140,120,50,70});
		System.out.println(heap.findMin());*/
		int i = 10;
		System.out.println(i--);
		System.out.println(i);
	}
	
}
