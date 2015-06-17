package Bing_Test.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/*Team 4Quarti
 * 
 * 
 * 
 * *
 */

public class ReaderBing {
	
	  private static ReaderBing istanza;

	  private ReaderBing()
	  {
	  }

	  public static ReaderBing getInstance()
	  {
	    if (istanza == null)
	    {
	      istanza = new ReaderBing();
	    }

	    return istanza; 
	  }


public List<String> getFile(String input) {
	
	List<String> query_list = new ArrayList<String>();
	
	BufferedReader br = null;
	 
	try {

		String sCurrentLine;

		br = new BufferedReader(new FileReader(input));

		while ((sCurrentLine = br.readLine()) != null) {
			query_list.add(sCurrentLine);
		}

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (br != null)br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	return query_list;
	
}

//public static void main(String args[]){
//	Reader r = new Reader();
//	List<String> query_list = r.getQuery("/Users/Marley1990/Desktop/4Quarti/query/part-m-00000");
//	for(String s : query_list)
//		System.out.println(s);
//}
	
	
}
