package com.rei.searchengine.repo;

import com.rei.searchengine.entities.SearchableDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rba on 10-Jul-22
 */
@Repository
public interface SearchableDocumentRepository extends ElasticsearchRepository<SearchableDocument, Long> {
}
