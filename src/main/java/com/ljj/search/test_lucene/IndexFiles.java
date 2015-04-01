package com.ljj.search.test_lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFiles {
	
    public static final String SOURCEPATH = Constant.USERDIR + File.separator + "source";

    public static void main(String[] args) throws Exception {
    	IndexFiles indexFiles = new IndexFiles();
    	File root = new File(SOURCEPATH);
    	File[] fs = root.listFiles();
    	for (int i=0; i<fs.length; i++) {
    		indexFiles.createIndex(fs[i]);
    	}
    }

    @SuppressWarnings("deprecation")
	public void createIndex(File sourceFile) throws Exception {
    	IndexWriter writer = createIndexWriter(OpenMode.CREATE);
    	System.out.println("文件路径：" + sourceFile.getAbsolutePath());
    	
    	Document doc = new Document();
    	doc.add(new Field("name", sourceFile.getName(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
    	
    	Field pathField = new Field("path", sourceFile.getPath(), Field.Store.YES, Field.Index.NO);
    	//pathField.setIndexOptions(IndexOptions.DOCS_ONLY);
    	doc.add(pathField);
    	
    	doc.add(new Field("modified", String.valueOf(sourceFile.lastModified()), Field.Store.YES, Field.Index.NO));
    	
    	String content = readFileContext(sourceFile);
    	doc.add(new Field("contents", content, Field.Store.YES, Field.Index.ANALYZED));
    	
    	if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
    		writer.addDocument(doc);
    	} else {
    		writer.updateDocument(new Term("path", sourceFile.getPath()), doc);
    	}
    	
    	writer.close();
    }
    
    public IndexWriter createIndexWriter(OpenMode openMode) throws Exception {
    	Directory dir = FSDirectory.open(new File(Constant.INDEXPATH));
    	IndexWriterConfig iwc = new IndexWriterConfig(Constant.VERSION, LuceneUtils.getAnalyzer());
    	iwc.setOpenMode(openMode);
    	IndexWriter writer = new IndexWriter(dir, iwc);
    	return writer;
    }
    
    @SuppressWarnings("resource")
	public static String readFileContext(File file) {
    	try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			StringBuilder content = new StringBuilder();
			for (String line=null; (line=br.readLine()) != null;) {
				content.append(line).append("\n");
			}
			return content.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
    }
}
