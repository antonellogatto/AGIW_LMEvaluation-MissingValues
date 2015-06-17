package Bing_Test.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.json.JSONException;

import Bing_Test.Query.QueryPig;
import Bing_Test.Request.MakeRequest;
import Bing_Test.lucene.DirectorCreateIndex;
import Bing_Test.lucene.DirectorQueryIndex;
import Bing_Test.util.ReaderBing;
import Bing_Test.util.Writer;

public class ExecuteQuerySecond {
	
	public ExecuteQuerySecond(){}

	public void execute() throws IOException, JSONException{
	
	/*SECONDA FASE
	 * 
	 * 
	 * 
	 * */
		QueryPig triple_to_queryForm = null;
		try {
			triple_to_queryForm = new QueryPig();
		} catch (ExecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	}
	
	/*COSTRUIAMO LE QUERY CON LM    
	 * NELLA FORMA 
	 * 			FILM PATTERN
	 * 
	 * */
	triple_to_queryForm.querySubRel("../Bing_Test/output/LM_matches.txt", 
			"new_query_dir");

	
	
	/*QUERY BING CON FILM PATTERN 
	 * 
	 * SALVIAMO I RISULTATI NEL FILE Input/film_dir.tsv
	 * 
	 * */
	String film_director = "../Bing_Test/Input/film_dir.tsv";
	MakeRequest mr = new MakeRequest();

	try {
		mr.makeRequestPhase2("../Bing_Test/new_query_dir/part-r-00000", film_director);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	/*CREIAMO L'INDICE PER OGNI REGISTA*/
	DirectorCreateIndex dci = new DirectorCreateIndex();
	dci.indexDirector();
	
	
	
	/*INTERROGHIAMO L'INDICE CON I PATTERN TROVATI E PRESENTI 
	 * NEL FILE Input/film_dir.tsv
	 * 
	 * 
	 * SALVIAMO I RISULTATI FILM DIRETTORE
	 * NEL FILE film_dir.txt
	 * */
	DirectorQueryIndex dqi = new DirectorQueryIndex();
	try {
		dqi.query(film_director, "film_dir.txt");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	/*CONTIAMO E RESTITUIAMO PER OGNI FILM 
	 * IL REGISTA CHE HA PIU OCCORRENZE
	 * */
	triple_to_queryForm.countDirByFilm("../Bing_Test/film_dir.txt","relation_count_dir");
	
	/* effettuiamo un join tra I direttori trovati e il file Registi
	 * 
	 *  e tra film e il file dei film per poi ricostruire le triple
	 *  
	 *  film, regista , Relazione
	 *  
	 *  */
	triple_to_queryForm.constructTriple("../Bing_Test/relation_count_dir/part-r-00000", "triple");
	
	
	
	/*Query con il nostro suo LM
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	List<String> lm = ReaderBing.getInstance().getFile("../Bing_Test/output/LM_matches.txt");
	List<String> our = ReaderBing.getInstance().getFile("../Bing_Test/output/LM_4Quarti.txt");
	
	String new_lm_path = "../Bing_Test/output/merge_lm.txt";
	
	List<String> merge = new ArrayList<String>(lm);
	merge.addAll(our);
	
	Writer.getInstance().write(merge,new_lm_path);
	
	
	triple_to_queryForm.querySubRel(new_lm_path, 
		"new_query_4Quarti");

	try {
		mr.makeRequestPhase2("../Bing_Test/new_query_4Quarti/part-r-00000", film_director);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		dqi.query(film_director, "film_dir4Quarti.txt");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	triple_to_queryForm.countDirByFilm("../Bing_Test/film_dir4Quarti.txt","relation_count_dir4quarti");

	
	triple_to_queryForm.constructTriple("../Bing_Test/relation_count_dir4quarti/part-r-00000", "triple4");

	
}
	
}
