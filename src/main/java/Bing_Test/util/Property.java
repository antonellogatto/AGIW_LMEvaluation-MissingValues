package Bing_Test.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Property{
	
	  private static Property istanza;

	  private Property()
	  {
	  }

	  public static Property getInstance()
	  {
	    if (istanza == null)
	    {
	      istanza = new Property();
	    }

	    return istanza; 
	  }
	
	public Map<String,String> getProperties(){
		Properties props = new Properties();
		Map<String,String> key_value = new HashMap<String,String>();
		try {
		//si carica il file
		props.load(new FileInputStream("../Bing_Test/conf.properties"));

		//si legge un particolare messaggio
		String input = props.getProperty("triple_file_path");
		key_value.put("triple_file_path", input);
		
		input = props.getProperty("film_file_path");
		key_value.put("film_file_path", input);

		input = props.getProperty("director_file_path");
		key_value.put("director_file_path", input);

		input = props.getProperty("lm_file_path");
		key_value.put("lm_file_path", input);

		String key = props.getProperty("keyBing");
		key_value.put("keyBing", key);
		}
		catch(IOException e)
		{
		e.printStackTrace();
		}
		return key_value;
	}
	
//	public static void main(String args[]){
//		Property p = new Property();
//		System.out.println(p.getProperties().get("keyBing"));
//	}

}
