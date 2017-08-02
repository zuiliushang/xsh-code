package xsh.raindrops.search.elasticsearch;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import xsh.raindrops.project.spring.elasticsearch.ElasticsearchConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=ElasticsearchConfig.class)
public class TestElasticSearch {
	
	@Autowired
	JestClient jestClient;
	
	String indexName = "zuiliushang";
	
	@Test
	public void createIndex() throws IOException {
		jestClient.execute(new CreateIndex.Builder("raindrops").build());
	}
	
	
	/**
	 * 索引已经存在，生产环境无需再创建
	 */

	@Test
	public void createIndex1() {
		CreateIndex createIndex =new CreateIndex.Builder(
				indexName).build();
		try {
			JestResult result = jestClient.execute(createIndex);
			if (result == null || !result.isSucceeded()) {
				throw new Exception(result.getErrorMessage() + "创建索引失败!");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Test
	public void test01() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("user","kimchy");
		json.put("postDate",new Date());
		json.put("message","trying out Elasticsearch");
	}
	
}
