package xsh.raindrops.struct.stack;

/**
 * 栈
 * @author Raindrops on 2017年5月16日
 */
public interface Stack<T> {
	// 返回堆栈大小
	int getSize();
	// 判断是否为空
	boolean isEmpty();
	// 入栈
	void push(T t);
	// 出栈
	T pop() throws StackEmptyException;
	// 取栈顶元素
	T peek()throws StackEmptyException;
}
