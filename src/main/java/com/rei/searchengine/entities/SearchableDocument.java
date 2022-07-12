package com.rei.searchengine.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @author rba on 10-Jul-22
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "docs")
public class SearchableDocument {
	private Long id;
	private List<String> tokens;
}

