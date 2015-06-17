package Bing_Test.lucene;

import java.io.IOException;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import Bing_Test.util.Property;
import Bing_Test.util.ReaderBing;


public class DirectorCreateIndex {

    /** Creates a new instance of Indexer */
    public DirectorCreateIndex() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            FileUtils.deleteDirectory(new File("../Bing_Test/DIRindex"));
            Directory indexDir = FSDirectory.open(new File("../Bing_Test/DIRindex"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer(Version.LUCENE_4_10_2, CharArraySet.EMPTY_SET));
            indexWriter = new IndexWriter(indexDir, config);
            config.setOpenMode(OpenMode.CREATE);
        }
        return indexWriter;
   }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }

    public void indexDirector() throws IOException {
    	
    	String input = Property.getInstance().getProperties().get("director_file_path");	
        List<String> lm_complete = ReaderBing.getInstance().getFile(input);
        IndexWriter writer = getIndexWriter(false);
        for(String s : lm_complete){
        	Document doc = new Document();
            doc.add(new TextField("name", s.split("\t")[1], Field.Store.YES));
        	writer.addDocument(doc);
        }
        closeIndexWriter();

    }

   
}
