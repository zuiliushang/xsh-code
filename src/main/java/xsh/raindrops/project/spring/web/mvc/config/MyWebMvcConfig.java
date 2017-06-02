package xsh.raindrops.project.spring.web.mvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import xsh.raindrops.project.view.interceptor.TokenInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="xsh.raindrops.project.spring.web")
public class MyWebMvcConfig extends WebMvcConfigurerAdapter{

	public MyWebMvcConfig() {
		System.out.println("MyWebMvcConfig");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
	}
	
}
