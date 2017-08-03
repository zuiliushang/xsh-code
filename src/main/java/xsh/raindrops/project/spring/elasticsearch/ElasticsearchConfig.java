package xsh.raindrops.project.spring.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jest.properties")
@ComponentScan("xsh.raindrops.project.spring.elasticsearch")
public class ElasticsearchConfig {

	public ElasticsearchConfig() {
		System.out.println("ElasticsearchConfig init");
	}

	@Value("${spring.elasticsearch.jest.uris}")
	String url;
	@Value("${spring.elasticsearch.jest.read-timeout}")
	Integer readTimeout;
	@Value("${spring.elasticsearch.jest.connection-timeout}")
	Integer connTimeout;
	
	/*@Bean
	public JestClient jestClient() {
		System.out.println(url);
		System.out.println(readTimeout);
		System.out.println(connTimeout);
		JestClientFactory jestClientFactory = new JestClientFactory();
		jestClientFactory.setHttpClientConfig(new HttpClientConfig
				.Builder(url)
				.gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create())  
                .connTimeout((connTimeout))  
                .readTimeout((readTimeout))  
                .multiThreaded(true)  
                .build());
		return jestClientFactory.getObject();
	}*/
	
	/*public Client client() {
		Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.ping_timeout", 1000)
                .put("discovery.zen.ping.multicast.enabled", "false").put("timeout", 1)
                .putArray("discovery.zen.ping.unicast.hosts", "l-flightdev18.f.dev.cn0.qunar.com:9300", "l-flightdev17.f.dev.cn0.qunar.com:9300")
                .build();
        Node node = NodeBuilder.nodeBuilder().clusterName("flight_fuwu_order_index").client(true).settings(settings).node();
        Client client = node.client();
	}*/
	
	
}
