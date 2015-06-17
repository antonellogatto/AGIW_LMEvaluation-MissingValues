package Bing_Test.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.mortbay.log.Log;


public class Writer {
	

	  private static Writer istanza;

	  private Writer()
	  {
	  }

	  public static Writer getInstance(){
	    if (istanza == null)
	    {
	      istanza = new Writer();
	    }

	    return istanza; 
	  }	
	  
	public void write(List<String> o,String input) throws JSONException{

		Log.info("WRITER sta scrivendo il file "+input+"\n");

        FileOutputStream prova;
		try {
			prova = new FileOutputStream(input);
		
        PrintStream scrivi = new PrintStream(prova);
        for(String obj : o){
       
            scrivi.println(obj);

        
		}
		Log.info("WRITER scrittura terminata ");

        scrivi.close();

		}
        catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
	
	public void difference(String input1,String input2, int int_max){
		
		Map<String,Integer> Key_Value = new TreeMap<String,Integer>();
		List<String> keyvalue = ReaderBing.getInstance().getFile(input1);
		for(String g : keyvalue){
			if(Integer.parseInt(g.split("\t")[1])>=int_max)
			Key_Value.put(g.split("\t")[0], Integer.parseInt(g.split("\t")[1]));
		}
		
		List<String> key = ReaderBing.getInstance().getFile(input2);
		for(String g : key){
			Key_Value.remove(g);
		}
		
		try {
			write(new ArrayList<String>(Key_Value.keySet()), "output/LM_4Quarti.txt");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
