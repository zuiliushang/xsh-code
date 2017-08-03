package xsh.raindrops.spring.tx;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import xsh.raindrops.project.config.MyBatisConfig;
import xsh.raindrops.project.service.TestService;
import xsh.raindrops.project.util.LogUtil;

public class Client {
	public static void main(String[] args) throws SecurityException, IOException {
		LogUtil.initLogger();
		ApplicationContext applicationContext =new AnnotationConfigApplicationContext(MyBatisConfig.class);
		TestService testService = applicationContext.getBean(TestService.class);
		testService.test();
	}
}
