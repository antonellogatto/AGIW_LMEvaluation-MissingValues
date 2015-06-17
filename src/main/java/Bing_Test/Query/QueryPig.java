package Bing_Test.Query;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.backend.executionengine.ExecException;
import org.mortbay.log.Log;

import Bing_Test.util.Property;


/*Team 4Quarti
 * 
 * 
 * 
 * *
 */


public class QueryPig{ 
    PigServer pigServer;

	public QueryPig() throws ExecException{
	pigServer = new PigServer(ExecType.LOCAL);	
	}
	

public  void toQueryForm(int limit) throws IOException {
	String input =  Property.getInstance().getProperties().get("triple_file_path");

    Log.info("PIG : "+input +" sta per essere trasformato in un file di query ");
	pigServer.registerQuery("myinput = LOAD '" +input+ "' as (id_s,s,id_o,o,relation);");
    pigServer.registerQuery("query = FOREACH myinput GENERATE CONCAT(CONCAT(s,' * '),o) as query_string;");
    pigServer.registerQuery("query2 = 	LIMIT query "+limit+";");
    FileUtils.deleteDirectory(new File("../Bing_Test/query_dir"));
    pigServer.store("query2","query_dir");

    pigServer.shutdown();
}

/*limit ?*/

public  void count(String input) throws IOException {
    Log.info("PIG : "+input +" ");
	pigServer.registerQuery("myinput = LOAD '" +input+ "' as (pattern);");
    pigServer.registerQuery("query = FOREACH myinput GENERATE pattern;");
    pigServer.registerQuery("grouped = GROUP query BY $0;");
    pigServer.registerQuery("count_raw = FOREACH grouped GENERATE group, COUNT(query) as con;");
    pigServer.registerQuery("ordered = ORDER count_raw BY con DESC;");
    pigServer.registerQuery("ordered_no_cont = FOREACH ordered GENERATE $0 ;");
    FileUtils.deleteDirectory(new File("../Bing_Test/count_dir"));
    pigServer.store("ordered","count_dir");
    FileUtils.deleteDirectory(new File("../Bing_Test/ordered_dir_no_cont"));
    pigServer.store("ordered_no_cont","ordered_dir_no_cont");
    pigServer.shutdown();
}

public  void countDirByFilm(String input,String output) throws IOException {
    Log.info("PIG : "+input +" ");
	pigServer.registerQuery("myinput = LOAD '" +input+ "' as (film,dir);");
    pigServer.registerQuery("rel = FOREACH myinput GENERATE film, dir;");
    pigServer.registerQuery("grouped = GROUP rel BY (film, dir);");
    pigServer.registerQuery("count_dir = FOREACH grouped GENERATE FLATTEN(group) , COUNT($1) as dircont;");
    pigServer.registerQuery("grouped2 = GROUP count_dir BY film;");
    pigServer.registerQuery("topResults = FOREACH grouped2 {result = "
    		+ "TOP(1, 2, count_dir); GENERATE FLATTEN(result);};");
    FileUtils.deleteDirectory(new File("../Bing_Test/"+output));
    pigServer.store("topResults",output);
    pigServer.shutdown();
}

public  void querySubRel(String input2,String outputDir) throws IOException {
	String input =  Property.getInstance().getProperties().get("film_file_path");
	Log.info("PIG : "+input +" ");
	pigServer.registerQuery("myinput = LOAD '" +input+ "' as (id_s,s);");
    pigServer.registerQuery("film = FOREACH myinput GENERATE s;");
	pigServer.registerQuery("rel = LOAD '" +input2+ "' as (relation);");
    pigServer.registerQuery("join2 = CROSS film, rel;");
    pigServer.registerQuery("query = FOREACH join2 GENERATE CONCAT(CONCAT(s,'_'),relation) as query;");
    FileUtils.deleteDirectory(new File("../Bing_Test/"+outputDir));
    pigServer.store("query",outputDir);

    pigServer.shutdown();
}

public  void constructTriple(String input,String outputDir) throws IOException {
    Log.info("PIG : "+input +" ");
    String input2 =Property.getInstance().getProperties().get("film_file_path");
    String input3 =Property.getInstance().getProperties().get("director_file_path");
	pigServer.registerQuery("filmdir = LOAD '" +input+ "' as (film,director,count);");
	pigServer.registerQuery("film = LOAD '" +input2+ "' as (id_f,f);");
	pigServer.registerQuery("dir = LOAD '" +input3+ "' as (id_d,d);");
    pigServer.registerQuery("filmtofilm = JOIN film BY f , filmdir BY film;");
    pigServer.registerQuery("dirtodir = JOIN filmtofilm BY director  , dir BY d;");
    pigServer.registerQuery("new_triple = FOREACH dirtodir GENERATE $0, $1,$5, $6,'film.film.directed_by';");
    FileUtils.deleteDirectory(new File("../Bing_Test/"+outputDir));
    pigServer.store("new_triple",outputDir);

    pigServer.shutdown();
}

}
