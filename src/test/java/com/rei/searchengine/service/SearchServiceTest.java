package com.rei.searchengine.service;

import com.rei.searchengine.cli.Printer;
import com.rei.searchengine.entities.SearchableDocument;
import com.rei.searchengine.repo.SearchableDocumentRepository;
import com.rei.searchengine.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SearchServiceTest {

    SearchService searchService;

    @Mock
    SearchableDocumentRepository repository;

    @Mock
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    MockedStatic<Printer> mockedPrinter;

    @BeforeEach
    void setUp() {
        openMocks(this);
        searchService = new SearchService(repository, elasticsearchRestTemplate);
        mockedPrinter = mockStatic(Printer.class);
    }

    @Test
    void index() {
        when(repository.save(any(SearchableDocument.class))).thenReturn(new SearchableDocument());
        searchService.index(1L, Collections.singletonList("test"));
        mockedPrinter.verify(() -> Printer.indexOk(anyLong(), anyLong()));

        // testing exceptions
        when(repository.save(any())).thenThrow(new IllegalArgumentException());
        searchService.index(1L, Collections.singletonList("test"));
        mockedPrinter.verify(() -> Printer.commandError(any(), any()));

        mockedPrinter.closeOnDemand();
    }

    @Test
    void query() {
        when(elasticsearchRestTemplate.search(any(Query.class), any()))
                .thenReturn(new SearchHitsImpl<>(0, TotalHitsRelation.EQUAL_TO, 0, null, Collections.emptyList(), null,null));
        searchService.query("test");
        mockedPrinter.verify(() -> Printer.queryResult(anyList(), anyLong()));

        //exception
        when(elasticsearchRestTemplate.search(any(Query.class), any())).thenThrow(new NullPointerException());
        searchService.query("test");
        mockedPrinter.verify(() -> Printer.commandError(any(), any()));

        mockedPrinter.closeOnDemand();
    }
}
