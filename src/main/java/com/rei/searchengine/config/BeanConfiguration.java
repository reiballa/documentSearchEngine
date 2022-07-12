package com.rei.searchengine.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author rba on 10-Jul-22
 */

@Configuration
public class BeanConfiguration {

	@Bean
	public RestHighLevelClient client() {
		ClientConfiguration clientConfiguration
				= ClientConfiguration.builder()
//				.connectedTo(System.getenv("ES_LOAD_BALANCER"))
				.connectedTo("localhost:9200")
				.build();

		return RestClients.create(clientConfiguration).rest();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchRestTemplate(client());
	}
}
