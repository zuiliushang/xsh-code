package xsh.raindrops.project.aop.aspect;

import javax.sql.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyJDBCAspectJ {
	
	@Autowired
	DataSource dataSource;
	
	private final String POINT_CUT = "execution(* leader.trans.*Service.save*(..)) || " + "execution(* leader.trans.*Service.update*(..)) ||"
			+ "execution(* leader.trans.*Service.delete*(..)) ||" + "execution(* leader.trans.*Service.create*(..))";
	
	/**
     * 配置前置通知,使用在方法aspect()上注册的切入点
     * 同时接受JoinPoint切入点对象,可以没有该参数
     */
    @Before(POINT_CUT)
    public void beforeTest(){
        System.out.println("before Test");
    } 
    
    @After(POINT_CUT)
    public void afterTest(){
    	System.out.println("after Test");
    }
	
	@Around(POINT_CUT)
	public Object writeAspect(ProceedingJoinPoint jp){
		Object object;
		try {
			object = jp.proceed();
			return object;
		} catch (Throwable e) {
			System.out.println();
			return null;
		}
		
	}
	
}
