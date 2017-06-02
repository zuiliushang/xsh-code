package xsh.raindrops.struct.tree;

/**
 * 二分查找树
 * 树中的每个节点 左节点都小于父节点，右节点都大于父节点
 * @author Raindrops on 2017年5月17日
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
	
	/**
	 * 二叉树节点
	 * @author Raindrops on 2017年5月17日
	 * @param <T>
	 */
	private static class BinaryNode<T>{
		T element; //节点元素
		BinaryNode<T> left;// 左节点
		BinaryNode<T> right;// 右节点
		public BinaryNode() {
			super();
			// TODO Auto-generated constructor stub
		}
		public BinaryNode(T element, BinaryNode<T> left, BinaryNode<T> right) {
			super();
			this.element = element;
			this.left = left;
			this.right = right;
		}
		
	}
	
	private BinaryNode<T> root;//根节点
	
	public BinarySearchTree() {
		root = null;
	}

	public void makeEmpty(){
		root = null;
	}

	public boolean isEmpty(){
		return root==null;
	}
	
	public boolean contains(T t){
		return contains(t,root);
	}
	
	public T findMin(){
		if (isEmpty()) {
			throw new UnderflowException();
		}
		return findMin(root).element;
	}
	
	public T findMax(){
		if (isEmpty()) {
			throw new UnderflowException();
		}
		return findMax(root).element;
	}
	
	public void insert(T t){
		//插入的时候重新赋值root
		root = insert(root,t);
	}
	
	public void remove(T t){
		//删除
		root = remove(root,t);
	}
	
	public void printTree(){
		if ( isEmpty() ) 
			System.out.println("二叉树为空");
		else
			printTree(root);
	}
	
	private void printTree(BinaryNode<T> t){
		if (t != null) {
			printTree(t.left);
			System.out.println(t.element);
			printTree(t.right);
		}
	}
	
	private boolean contains(T t,BinaryNode<T> node){
		if (node==null) 
			return false;
		int status = t.compareTo(node.element);
		if (status<0) 
			return contains(t,node.right);
		else if (status>0)
			return contains(t, node.left);
		else 
			return true;
	}
	
	
	/**
	 * 查找最小值
	 * @param node
	 */
	private BinaryNode<T> findMin(BinaryNode<T> node){
		if (node == null) 
			return null;
		else if (node.left == null)
			return node;
		return findMin(node.left);
	}
	/**
	 * 查找最大值
	 * @param node
	 */
	private BinaryNode<T> findMax(BinaryNode<T> node){
		if (node == null) 
			return null;
		else if (node.right == null)
			return node;
		return findMax(node.right);
	}	
	
	/**
	 * 插入二叉树
	 * @param node
	 * @param t
	 * @return
	 */
	private BinaryNode<T> insert(BinaryNode<T> node , T t){
		if (node == null) 
			return new BinaryNode<T>(t, null, null);
		int status = t.compareTo(node.element);
		if (status < 0)
			 node.left = insert(node.left,t);
		else if(status > 0)
			 node.right = insert(node.right, t);
		else 
			;
		return node;
	}
	
	private BinaryNode<T> remove(BinaryNode<T> node, T t){
		// t不存在 返回node
		if (t == null) 
			return node;
		int status = t.compareTo(node.element);
		if (status < 0)
			node.left = remove(node.left, t);
		else if (status > 0)
			node.right = remove(node.right, t);
		else if( node.left != null && node.right != null)//左子叶和右子叶存在
		{
			node.element = findMin( node.right ).element;//把右节点的最左边的节点提取出来进行重组
			node.right = remove(node.right, node.element);//删除右边中最左边的节点
		}
		else
			node = (node.left != null) ? node.left:node.right;
		return node;
	}	
	
	public static void main(String[] args){
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();
		tree.printTree();
		tree.insert(5);
		tree.insert(1);
		tree.insert(4);
		tree.insert(11);
		tree.insert(8);
		tree.printTree();
		tree.remove(4);
		tree.printTree();
		System.out.println(tree.contains(5));
	}
}
