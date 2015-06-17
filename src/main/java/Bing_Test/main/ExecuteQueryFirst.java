package Bing_Test.main;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.pig.backend.executionengine.ExecException;
import Bing_Test.Query.QueryPig;
import Bing_Test.Request.MakeRequest;
import Bing_Test.lucene.LMCreateIndex;
import Bing_Test.lucene.LMQueryIndex;
import Bing_Test.util.Writer;

public class ExecuteQueryFirst {
	
	public ExecuteQueryFirst(){}

	public  void execute() throws IOException{
		
		/*PRIMA FASE*/
		

		/* Creiamo il file di query dal file di triple
		 * 
		 * usato poi da MakeRequest
		 *    */
		QueryPig triple_to_queryForm = null;
		try {
			triple_to_queryForm = new QueryPig();
//			triple_to_queryForm.toQueryForm(100);
		} catch (ExecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	}
		
		
		/* effettuiamo le query
		 * e prendiamo dal risultato tutte le descrizioni
		 * che vengono poi analizzate, ripulite e/o scartate
		 * dal Parser che crea un file clean_pattern.txt, con pattern
		 * da validare con il file del LM
		 * 
		 * */
//		MakeRequest mr = new MakeRequest();
//		try {
//			mr.makeRequest("../Bing_Test/query_dir/part-r-00000","../Bing_Test/clean_pattern.txt");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		/*Indicizziamo i pattern esatti del Language Model*/
//		LMCreateIndex ba = new LMCreateIndex();
//		try {
//			ba.indexLM();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		/* Contiamo e restituiamo i pattern in ordine*/
//		triple_to_queryForm.count("../Bing_Test/clean_pattern.txt");
		
		
		/*Effettuiamo le query e ritorniamo gli EXACT MATCH
		 * e gli stampio in un file per farne uso nella
		 * seconda fase
		 * 
		 * Effettuando magari delle statistiche su quali sono i maggiori match
		 * */
		
		LMQueryIndex qi = new LMQueryIndex();
		try {
			qi.query("../Bing_Test/ordered_dir_no_cont/part-r-00000",
					"output/LM_matches.txt");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*  Troviamo nuovi match da usare nella seconda fase
		 * 
		 *  effettuiamod elle statistiche per vedere quali sono i maggiori
		 *  ../Bing_Test/count_dir/part-r-00000 : contiene i match trovati con il numero di occorrenze
		 *  ../Bing_Test/output/LM_matches.txt : contiene gli exact match con LM
		 *  ../Bing_Test/output/LM_4Quarti.txt : contiene i nostri nuovi pattern del NOSTRO LM
		 *  
		 *  */
		
		
		Writer.getInstance().difference("../Bing_Test/count_dir/part-r-00000", "../Bing_Test/output/LM_matches.txt", 500);
		
		
		
		
		

	}
	
}
