package xsh.raindrops.project.spring.bean.process.professor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 1、实现BeanFactoryPostProcessor 接口，会被Application contexts自动发现 
 * 2、BeanFactoryPostProcessor 仅仅对 bean definitions 发生关系，
 * 不能对bean instances 交互，对bean instances 的交互，由BeanPostProcessor的实现来处理 
 * 3、PropertyResourceConfigurer 是一个典型的实现 
 * @author Raindrops on 2017年5月15日
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor{

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("=================");
		System.out.println("MyBeanFactoryPostProcessor");
		System.out.println("=================");
	}
	
}
