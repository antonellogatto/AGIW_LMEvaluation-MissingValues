package Bing_Test.Request;

import java.io.DataInputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.log.Log;

import Bing_Test.parse.Parser;
import Bing_Test.util.Property;
import Bing_Test.util.ReaderBing;
import Bing_Test.util.Writer;

/*	Team 4Quarti	*/

public class MakeRequest {


	public  void makeRequest(String input, String output) throws Exception {
		byte[] b = new byte[1];
		setUp();
		
		List<String> query_list = ReaderBing.getInstance().getFile(input);
		List<String> temp = new ArrayList<String>();
		Parser p = new Parser();
		for(int i=0;i<query_list.size();i++){
			temp.add(query_list.get(i));
		}

		for(String query : temp){
			Log.info("MAKEREQUEST sta effettuando la query : " +query);
			List<JSONObject> list = new ArrayList<JSONObject>();
			URL u = new URL("https://api.datamarket.azure.com/Bing/SearchWeb/Web?"
					+ "Query=%27"+query.replaceAll(" ","%20").replaceAll("&","%26")+"%27&$format=json");
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			DataInputStream di = new DataInputStream(con.getInputStream());
			String response = "";
			while (-1 != di.read(b, 0, 1)) {
				response = response+ new String(b,"UTF-8");
			}

			JSONObject temp1 = new JSONObject(response);
			list.add(temp1);

			p.findPattern(list,query);

		}

		List<String> clean_matches = p.clean();

		try {
			Writer.getInstance().write(clean_matches, output);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public  void makeRequestPhase2(String input, String output) throws Exception {
		byte[] b = new byte[1];
		setUp();

		List<String> query_list = ReaderBing.getInstance().getFile(input);
		List<String> temp = new ArrayList<String>();
		Parser p = new Parser();
		List<String> clean_dir = new ArrayList<String>();
		
		for(int i=0;i<query_list.size();i++){
			temp.add(query_list.get(i));
		}

		for(String query : temp){
			Log.info("MAKEREQUEST sta effettuando la query : " +query+"\n");
			List<JSONObject> list = new ArrayList<JSONObject>();
			URL u = new URL("https://api.datamarket.azure.com/Bing/SearchWeb/Web?"
					+ "Query=%27%22"+URLEncoder.encode(query.replaceAll("_", " "),"UTF-8")+"%22%27&$format=json");
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			DataInputStream di = new DataInputStream(con.getInputStream());
			String response = "";
			while (-1 != di.read(b, 0, 1)) {
				response = response+ new String(b,"UTF-8");
			}

			JSONObject temp1 = new JSONObject(response);
			list.add(temp1);
			
			p.findDirector(list, query);
		}

		 
		clean_dir.addAll(p.getDirs());
		try {
			Writer.getInstance().write(clean_dir, output);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void setUp() {
		Properties systemSettings = System.getProperties();
		systemSettings.put("http.proxyHost", "proxy.mydomain.local");
		systemSettings.put("http.proxyPort", "80");


		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				String key = Property.getInstance().getProperties().get("keyBing");
				return new PasswordAuthentication(key,key.toCharArray());
			}
		});
	}


}