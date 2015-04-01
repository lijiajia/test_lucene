package com.ljj.search.test_lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class SearchFiles {

    public static void main(String[] args) throws Exception {
    	SearchFiles searchFiles = new SearchFiles();
    	searchFiles.search("消失");
    }

    public void search(String queryString) throws Exception {
    	String[] queryFileds = {"contents"};
    	IndexSearcher searcher = createIndexSearch();
    	Query query = createQuery(queryFileds, queryString);
    	
    	int queryCount = 100;
    	TopDocs results = searcher.search(query, null, queryCount);
    	System.out.println("总符合:" + results.totalHits + "条数!");
    	
    	for (ScoreDoc sr : results.scoreDocs) {
    		int docID = sr.doc;
    		Document doc = searcher.doc(docID);
    		System.out.println("name = " + doc.get("name"));
    		System.out.println("path = " + doc.get("path"));
    		System.out.println("modified = " + doc.get("modified"));
    		System.out.println("contents = " + doc.get("contents"));
    	}
    }
    
    @SuppressWarnings("deprecation")
	public static IndexSearcher createIndexSearch() throws CorruptIndexException, IOException {
    	IndexReader reader = IndexReader.open(FSDirectory.open(new File(Constant.INDEXPATH)));
    	IndexSearcher searcher = new IndexSearcher(reader);
    	return searcher;
    }
    
    public static Query createQuery(String[] queryFileds, String queryString) throws ParseException {
    	QueryParser parser = new MultiFieldQueryParser(Constant.VERSION, queryFileds, LuceneUtils.getAnalyzer());
    	Query query = parser.parse(queryString);
    	return query;
    }
}
