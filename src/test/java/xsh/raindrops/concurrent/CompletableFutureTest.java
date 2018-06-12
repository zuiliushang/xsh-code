package xsh.raindrops.concurrent;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import xsh.raindrops.project.entity.User;

/**
 * 
 * @author Raindrops
 * @date 2018-04-18
 *
 */
public class CompletableFutureTest {

	/**
	 * 运行有关
	 */
	@Test
	public void thenApply() {
		String result = CompletableFuture.supplyAsync(()->"hello").thenApplyAsync( t -> t+" world")
				.thenApply(t-> t + "!").join();
		System.out.println(result);
	}
	
	@Test
	public void testThenApply() {
		String result = CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			System.out.println("supply");
			return "hello";
		}).thenRun(()->System.out.println("run")).thenApplyAsync(t -> {
			System.out.println("apply");
			return t + " world";
		}).join();
		System.out.println("h");
	}
	
	/**
	 * 运行无关
	 */
	@Test
	public void thenRun() {
		String result = CompletableFuture.supplyAsync(()->"hello").thenRun(()->System.out.println("hello world run !")).thenApply(t->t+"rain").join();
	}
	
	/**
	 * 2个CompletionStage获取后整合 转化其他类型结果返回
	 */
	@Test
	public void thenCombine() {
		Integer result = CompletableFuture.supplyAsync(() -> "hello").thenCombineAsync(CompletableFuture.supplyAsync(()-> " world"), (t1,t2)->1).join();
		System.out.println(result);
	}
	
	/**
	 * 2个执行后进行 消耗
	 */
	@Test
	public void thenAcceptBoth() {
		CompletableFuture<Void> result = CompletableFuture.supplyAsync(()->"hello").thenAcceptBothAsync(CompletableFuture.supplyAsync(()->" world!"), (t1,t2)->System.out.println(t1 + t2));
	}
	
	/**
	 * 两个运行后再运行runable
	 */
	@Test
	public void runAfterBoth() {
		CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "hello";
			}).runAfterBoth(CompletableFuture.supplyAsync(()->"ok"),
					()->System.out.println("haha"));
	}
	
	/**
	 * 运行得块的结果 进行转化生成
	 */
	@Test
	public void applyToEither() {
		CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return "hello ";
			}).applyToEitherAsync(CompletableFuture.supplyAsync(()->"world "), t->1).join();
	}
	
	/**
	 * 运行的快的结果进行消费
	 */
	@Test
	public void acceptEither() {
		User user = new User();
		CompletableFuture.supplyAsync(()->{
			System.out.println("hello");
			//user.setName("raindrops");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("hello");
			user.setName("raindrops");
			return "hello";
		}).acceptEitherAsync(CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("world");
			return "world";
		}), System.out::println).join();
		System.out.println("hello" + user.getName());
	}
	
	/**
	 * 任何一个先运行完了运行runable
	 */
	@Test
	public void runAfterEither() {
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "hello" ;
		}).runAfterEitherAsync(CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "world";
		}),()->System.out.println(Thread.currentThread().getName())).join();
	}
	
	/**
	 * 异常了补偿执行
	 */
	@Test
	public void exceptionally() {
		CompletableFuture.supplyAsync(()->1/0+"").exceptionally(e->e.getMessage()).thenAccept(System.out::println);
	}
	
	/**
	 * 异常时记录状态
	 */
	@Test
	public void whenComplete() {
		CompletableFuture.supplyAsync(()->{
			int i = 1/0;
			return "hello";
		}).acceptEitherAsync(CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return "world";
		}),System.out::println).whenComplete((s,e)->{
			System.out.println(s);
			System.out.println(e.getMessage());
		});
	}
	
	@Test
	public void whenComplete1() {
		CompletableFuture.supplyAsync(()->{
			int i = 1/0;
			return "hello";
		}).whenComplete((t,e)->{
			System.out.println(t);
			System.out.println(e.getMessage());
		}).exceptionally(e->e.getMessage()).join();
	}
	
	/**
	 * 运行完成时，对结果的处理。
	 * 这里的完成时有两种情况，
	 * 一种是正常执行，返回值。
	 * 另外一种是遇到异常抛出造成程序的中断。
	 */
	@Test
	public void handle() {
		String result = CompletableFuture.supplyAsync(()->{
			//int i = 1/0 ;
			return "hello";
		}).handle((s,e)->{
			if(e != null) {
				return e.getMessage();
			}
			System.out.println(s);
			return s;
		}).join();
		System.out.println(result);
	}
	
	@Test
	public void thenCompose() {
		CompletableFuture.supplyAsync(()->"hello world")
			.thenComposeAsync(t->CompletableFuture.supplyAsync(()->t + " raindrops"))
			.thenAccept(System.out::println).join();
	}
	
}

