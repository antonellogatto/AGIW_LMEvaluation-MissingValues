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

public class DirectorQueryIndex {
	public void query(String input,String output) throws IOException, ParseException{
		/* set the maximum number of results */
		int maxHits = 1;
		String index = "../Bing_Test/DIRindex";
		/* open a directory reader and create searcher and topdocs */
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);

		/* query string */

		List<String> query_list = ReaderBing.getInstance().getFile(input);
		List<String> match = new ArrayList<String>();
		for(String querystring : query_list){
				String film = querystring.split("\t")[0];
				String director = querystring.split("\t")[1];
				
				PhraseQuery q = null;
				PhraseQuery q2 = null;

				String[] director_list = director.split(" ");
				if(director_list.length==3){
					q=new PhraseQuery();
					q2=new PhraseQuery();
				for(int j =0 ; j<director_list.length-1;j++){
					q.add(new Term("name", director_list[j].toLowerCase()));
				}
				q2.add(new Term("name", director_list[1].toLowerCase()));
				q2.add(new Term("name", director_list[2].toLowerCase()));
				q.setSlop(0);
				q2.setSlop(0);
				}
				else{
					q=new PhraseQuery();
					q.add(new Term("name", director_list[0].toLowerCase()));
					q.add(new Term("name", director_list[1].toLowerCase()));
				}
				TopScoreDocCollector collector =
						TopScoreDocCollector.create(maxHits, true);
				/* search into the index */
				searcher.search(q, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				System.out.println("Query 1 : "+q.toString());
				if(hits.length==0){
					if(q2!=null){
						 collector =
								TopScoreDocCollector.create(maxHits, true);
						searcher.search(q2, collector);
						hits = collector.topDocs().scoreDocs;
						System.out.println("Query 2 : "+q2.toString());
						}
				}
				

				
				/* print results */
				for(int i=0;i<hits.length;i++) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					System.out.println(d.get("name"));
//					if(d.get("name").toLowerCase().contains(director.toLowerCase()))
						match.add(film+"\t"+d.get("name"));
					
			}
		}

		try {
			Writer.getInstance().write(match,output);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	} // end method

}
