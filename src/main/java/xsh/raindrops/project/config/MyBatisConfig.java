package xsh.raindrops.project.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

@Configuration
@ComponentScan("xsh.raindrops.project")
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
@MapperScan("org.leader.us.mapper")
public class MyBatisConfig {
	@Value("${driver}")
	private String driver;//DB Driver
	@Value("${url}")
	private String url;//DB url
	@Value("${root}")
	private String root;//DB username
	@Value("${password}")
	private String password;//DB password
	//注入DataSource
	@Bean
	public DataSource dataSource(){
		BasicDataSource dataSource = new BasicDataSource();//配置dbcp连接池
		dataSource.setDriverClassName(driver);//驱动
		dataSource.setUrl(url);//DB链接
		dataSource.setUsername(root);//用户名
		dataSource.setPassword(password);//密码
		return dataSource;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(){
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		SqlSessionFactory sessionFactory = null;
		try {
			sessionFactory = sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.notNull(sessionFactory,"sessionFactory must not null");
		return sessionFactory;
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(){
		SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
		return sessionTemplate;
	}
	
	@Bean
	public PlatformTransactionManager getTransactionManager()
	{
		DataSourceTransactionManager txMan=new DataSourceTransactionManager();
		txMan.setDataSource(dataSource());
		return txMan;
	}
	
}