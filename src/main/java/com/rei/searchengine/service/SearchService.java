package com.rei.searchengine.service;

import com.rei.searchengine.cli.Printer;
import com.rei.searchengine.entities.Command;
import com.rei.searchengine.entities.SearchableDocument;
import com.rei.searchengine.repo.SearchableDocumentRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsible for saving documents and querying them
 * @author rba on 11-Jul-22
 */
@Service
public class SearchService {

	private final SearchableDocumentRepository repo;
	private final ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	public SearchService(SearchableDocumentRepository searchableDocumentRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
		this.repo = searchableDocumentRepository;
		this.elasticsearchRestTemplate = elasticsearchRestTemplate;
	}

	/**
	 * Saves a SearchableDocument in the elasticSearch index.
	 * If the id already exists, that document is overridden by the new one.
	 *
	 * @param docId Long id of the new document
	 * @param tokens List of token strings
	 */
	public void index(Long docId, List<String> tokens) {
		try {
			long start = System.currentTimeMillis();
			SearchableDocument document = new SearchableDocument(docId, tokens);
			repo.save(document);
			Printer.indexOk(docId, System.currentTimeMillis() - start);
		} catch (Exception e) {
			Printer.commandError(Command.INDEX.getValue(), e.getMessage());
		}
	}

	/**
	 * Executes query provided as input and prints the ids of matched documents
	 *
	 * @param queryExpression the query string that needs to be executed
	 */
	public void query(String queryExpression) {
		try {
			long start = System.currentTimeMillis();
			Query query = new NativeSearchQueryBuilder()
					.withQuery(QueryBuilders.queryStringQuery(queryExpression))
					.build();
			SearchHits<SearchableDocument> result = elasticsearchRestTemplate.search(query, SearchableDocument.class);
			List<Long> documentIds = result.getSearchHits()
					.stream()
					.map(SearchHit::getContent)
					.map(SearchableDocument::getId)
					.collect(Collectors.toList());
			Printer.queryResult(documentIds, System.currentTimeMillis() - start);
		} catch (Exception e) {
			Printer.commandError(Command.QUERY.getValue(), e.getMessage());
		}
	}
}
