package Bing_Test.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONException;

import Bing_Test.util.ReaderBing;
import Bing_Test.util.Writer;
public class LMQueryIndex {


	public LMQueryIndex(){}

	public void query(String input,String output) throws IOException, ParseException{
		/* set the maximum number of results */
		int maxHits = 6000;
		String index = "../Bing_Test/LMindex";
		/* open a directory reader and create searcher and topdocs */
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);

		/* query string */

		List<String> query_list = ReaderBing.getInstance().getFile(input);
		List<String> match = new ArrayList<String>();
		for(String querystring : query_list){
			if(querystring.split(" ").length<=4){

				PhraseQuery q = new PhraseQuery();
				String[] list_term = querystring.split(" ");
				for(int j =0 ; j<list_term.length;j++){
					q.add(new Term("pattern", list_term[j].toLowerCase()));
				}			
				q.setSlop(0);

				TopScoreDocCollector collector =
						TopScoreDocCollector.create(maxHits, true);
				/* search into the index */
				searcher.search(q, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;

				
				/* print results */
				for(int i=0;i<hits.length;i++) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					if(d.get("pattern").toLowerCase().equals(querystring.toLowerCase()))
						match.add(d.get("pattern"));
					}
			}
		}

		try {
			Writer.getInstance().write(match,output);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	} // end method


//	public static void main(String args[]){
//		QueryIndex q =  new QueryIndex();
//		try {
//			q.query("../Bing_Test/ordered_dir_no_cont/part-r-00000",
//					"output/LM_matches.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
