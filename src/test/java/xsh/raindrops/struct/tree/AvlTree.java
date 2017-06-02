package xsh.raindrops.struct.tree;


/**
 * 平衡二叉树 
 * 平衡二叉树允许每个节点的左子树和右子树高度不超过1 
 * @author Raindrops on 2017年5月17日
 */
public class AvlTree<T extends Comparable<? super T>>{
	
	/**
	 * 平衡二叉树节点
	 * @author Raindrops on 2017年5月17日
	 *
	 * @param <T>
	 */
	private static class AvlNode<T>{
		T element;
		AvlNode<T> left;
		AvlNode<T> right;
		int height;
		public AvlNode() {
			super();
		}
		public AvlNode(T element, AvlNode<T> left, AvlNode<T> right) {
			super();
			this.element = element;
			this.left = left;
			this.right = right;
			this.height = 0;
		}
	}
	
	private AvlNode<T> root;
	
	public AvlTree() {
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
		if (root==null)
			throw new UnderflowException();
		return findMin(root).element;
	}
	
	public T findMax(){
		if (root == null) 
			throw new UnderflowException();
		return findMax(root).element;
	}
	
	public void insert(T t){
		root = insert(root,t);
	}
	
	private boolean contains(T t,AvlNode<T> node){
		if (node == null) {
			return false;
		}
		int status = node.element.compareTo(t);
		if ( status > 0)
			return contains(t, node.left);
		else if ( status < 0)
			return contains(t, node.right);
		else 
			return true;
	}
	
	private AvlNode<T> findMin(AvlNode<T> t){
		if (t == null) 
			return null;
		else if(t.left == null)
			return t;
		else
			return findMin(t.left);
	}	
	
	private AvlNode<T> findMax(AvlNode<T> t){
		if(t == null)
			return null;
		else if (t.right == null)
			return t;
		else
			return findMax(t.right);
	}
	
	private AvlNode<T> insert(AvlNode<T> node, T t){
		if (node == null) {
			return new AvlNode<T>(t, null, null);
		}
		int status = node.element.compareTo(t);
		if (status > 0)
			node.left =  insert(node.left,t);
		else if (status < 0)
			node.right = insert(node.right, t);
		else
			;
		return node;
	}
	
}
