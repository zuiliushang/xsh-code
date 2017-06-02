package xsh.raindrops.project.aop.aspect;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.Assert;

/**
 * 编程式实现AspectJ事务拦截
 * @author Raindrops on 2017年5月15日
 */
@Configuration
@PropertySource(value="classpath:jdbc.properties")
@EnableAspectJAutoProxy
public class MyAspectJTxConfig {
	@Value("${driver}")
	private String driver;//DB Driver
	@Value("${url}")
	private String url;//DB url
	@Value("${root}")
	private String root;//DB username
	@Value("${password}")
	private String password;//DB password
	@Value("${initialPoolSize}")
	private Integer initialPoolSize;
	@Value("${maxActive}")
	private Integer maxActive;
	@Value("${maxIdle}")
	private Integer maxIdle;
	@Value("${minIdle}")
	private Integer minIdle;
	@Value("${maxWait}")
	private Integer maxWait;
	@Value("${logAbandoned}")
	private boolean logAbandoned;
	@Value("${removeAbandoned}")
	private boolean removeAbandoned;
	@Value("${removeAbandonedTimeout}")
	private Integer removeAbandonedTimeout;
	@Value("${timeBetweenEvictionRunsMillis}")
	private Integer timeBetweenEvictionRunsMillis;
	@Value("${numTestsPerEvictionRun}")
	private Integer numTestsPerEvictionRun;
	@Value("${minEvictableIdleTimeMillis}")
	private Integer minEvictableIdleTimeMillis;
	//注入DataSource
	@Bean
	public DataSource dataSource(){
		BasicDataSource dataSource = new BasicDataSource();//配置dbcp连接池
		dataSource.setDriverClassName(driver);//驱动
		dataSource.setUrl(url);//DB链接
		dataSource.setUsername(root);//用户名
		dataSource.setPassword(password);//密码
		dataSource.setInitialSize(initialPoolSize);//初始化连接
		dataSource.setMinIdle(minIdle);//最小连接数
		dataSource.setMaxActive(maxActive);//最大连接数
		dataSource.setMaxIdle(maxIdle);//最大空闲数量
		dataSource.setMaxWait(maxWait);//超时等待时间以毫秒
		dataSource.setLogAbandoned(logAbandoned);//连接池泄露时是否打印
		dataSource.setRemoveAbandoned(removeAbandoned);//是否自动回收超时连接
		dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);//超时时间 单位秒
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);//在空闲连接回收器线程运行期间休眠的时间值,以毫秒为单位
		dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);//在每次空闲连接回收器线程(如果有)运行时检查的连接数量
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);//连接在池中保持空闲而不被空闲连接回收器线程
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
	
	@Bean
	public TransactionInterceptor transactionInterceptor(){
		TransactionInterceptor txInterceptor = new TransactionInterceptor();
		txInterceptor.setTransactionAttributeSource(nameMatchTransactionAttributeSource());
		return txInterceptor;
	}
	@Bean
	public NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource(){
		NameMatchTransactionAttributeSource ntas = new NameMatchTransactionAttributeSource();
		RuleBasedTransactionAttribute readOnlyTx = 
				new RuleBasedTransactionAttribute();
		readOnlyTx.setReadOnly(true);
		RuleBasedTransactionAttribute requiredTx =
				new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, null);
		RuleBasedTransactionAttribute requiredNewTx =
				new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW, null);
		RuleBasedTransactionAttribute nestedTx = 
				new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_NESTED, null);
		Map<String, TransactionAttribute> txMaps = new HashMap<>();
		txMaps.put("add*", requiredTx);
		txMaps.put("update*", requiredNewTx);
		txMaps.put("query*", readOnlyTx);
		txMaps.put("del*", nestedTx);
		ntas.setNameMap(txMaps);
		return ntas;
	}
	/**
	 * 编程式实现 Aspect
	 */
	@Bean
	public AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor(){
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setAdvice(transactionInterceptor());
		advisor.setExpression("execution(* org.leader.us.aspectJ.*.*(..)) ");
		return advisor;
	}
}
