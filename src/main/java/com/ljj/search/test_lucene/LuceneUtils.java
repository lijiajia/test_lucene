package com.ljj.search.test_lucene;

import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneUtils {
    public static Analyzer getAnalyzer() {
    	Analyzer analyzer = new IKAnalyzer();
    	return analyzer;
    }
}
