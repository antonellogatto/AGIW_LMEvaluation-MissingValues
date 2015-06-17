package Bing_Test.parse;
//import java.text.Normalizer;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*Team 4Quarti
 * 
 * 
 * 
 * *
 */


public class Parser{ 


	private List<String> matches;
	private List<String> dirs;

	public Parser(){
		matches =   new ArrayList<String>();
		dirs =   new ArrayList<String>();

	}

	public List<String> getDirs() {
		return dirs;
	}

	public void setDirs(List<String> dirs) {
		this.dirs = dirs;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	public  void findPattern(List<JSONObject> list , String query) throws JSONException{
		//		Log.info("PARSER sta effettuando il match con la query : " +query);
		String match = null;

		String film = query.split("\\*")[0];
		String reg = query.split("\\*")[1];
		match = film.substring(0,film.length()-1)+"(.*.)"+reg.substring(1,reg.length());

		for(JSONObject obj : list){
			JSONArray ajson = obj.getJSONObject("d").getJSONArray("results");
			for(int i= 0; i<ajson.length();i++){
				String temp = ajson.getJSONObject(i).get("Description").toString();
				Pattern pattern = Pattern.compile(match);
				Matcher matcher = pattern.matcher(temp);
				if (matcher.find())
				{
					matches.add(" "+matcher.group(1)+"   ");
				}
			}
		}


	}


	public List<String> clean(){
		//		Log.info("PARSER sta ripulendo i match trovati " );


		/*manca da trasformare i minuti con la stringa MIN
		 * e stogliere lo spazio iniziale nelle frasi
		 * 
		 * */
		List<String> clean_matches = new ArrayList<String>();
		for(String s : matches){

			String f = fixEncoding(s);

			String l = f.toLowerCase()
					.replaceAll("\\s([0-9][0-9][0-9])\\s", " MIN ")
					.replaceAll("/\\./", " ").replaceAll("/\\(/", "")
					.replaceAll("-","")
					.replaceAll("!", "").replaceAll("\\?+", "")
					.replaceAll(":",  " ").replace(',','.')
					.replaceAll("\\(", "").replaceAll("\\)","").replaceAll("\\.", "")
					.replaceAll(";", "").replace("'", "")
					.replaceAll("[|]","").replace("[","").replace("]", "")
					.replace("\"","")
					.replaceAll("(([0-3][0-9])??(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))??([0-9][0-9][0-9][0-9])"
							, "DATE")
							.replaceAll("\\s+\\s+", "");
			;
			if(!l.equals("")){
				clean_matches.add(l);}
		}

		return clean_matches;


	}

	public void findDirector(List<JSONObject> list, String query) throws JSONException {
		//		Log.info("PARSER sta effettuando il match con la query : " +query);

		for(JSONObject obj : list){
			JSONArray ajson = obj.getJSONObject("d").getJSONArray("results");
			for(int i= 0; i<ajson.length();i++){
				String temp = ajson.getJSONObject(i).get("Description").toString();
				Pattern pattern = Pattern.compile(query.toLowerCase().replaceAll("_"," ")+" (.*)");
				Matcher matcher = pattern.matcher(clean2(temp).toLowerCase());
				if (matcher.find() && matcher.groupCount()>=1)
				{
					if( matcher.group(1).split(" ").length>=3){
						String s1 = matcher.group(1).split(" ")[0];
						String s2 = matcher.group(1).split(" ")[1];
						String s3 = matcher.group(1).split(" ")[2];
						dirs.add(query.split("_")[0]+"\t"+s1+" "+s2+" "+s3);
					}else if(matcher.group(1).split(" ").length>=2){
						String s1 = matcher.group(1).split(" ")[0];
						String s2 = matcher.group(1).split(" ")[1];
						dirs.add(query.split("_")[0]+"\t"+s1+" "+s2);
					}
				}
			}
		}
	}
	public String clean2(String temp){
		//		Log.info("PARSER sta ripulendo i match trovati " );


		/*manca da trasformare i minuti con la stringa MIN
		 * e stogliere lo spazio iniziale nelle frasi
		 * 
		 * */


		String f = fixEncoding(temp);

		String l = f.toLowerCase().replaceAll("_"," ")
				.replaceAll("\\s([0-9][0-9][0-9])\\s", "").replaceAll("_"," ")
				.replaceAll("/\\./", " ").replaceAll("/\\(/", " ")
				.replaceAll("-"," ")
				.replaceAll("!", " ").replaceAll("\\?+", " ")
				.replaceAll(":",  " ").replace(',','.')
				.replaceAll("\\(", " ").replaceAll("\\)"," ").replaceAll("\\.", " ")
				.replaceAll(";", " ").replace("'", " ")
				.replaceAll("[|]"," ").replace("["," ").replace("]", " ")
				.replace("\"","")
				.replaceAll("(([0-3][0-9])??(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))??([0-9][0-9][0-9][0-9])"
						, "")
						.replaceAll("\\s+\\s+", " ");


		return l;

	}
	/* Togliamo gli accenti ??????*/
	//private String deAccent(String str) {
	//    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	//    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	//    return pattern.matcher(nfdNormalizedString).replaceAll("");
	//}

	public static String fixEncoding(String latin1) {
		try {
			byte[] bytes = latin1.getBytes("ISO-8859-1");
			if (!validUTF8(bytes))
				return latin1;   
			return new String(bytes, "UTF-8");  
		} catch (UnsupportedEncodingException e) {
			// Impossible, throw unchecked
			throw new IllegalStateException("No Latin1 or UTF-8: " + e.getMessage());
		}

	}

	public static boolean validUTF8(byte[] input) {
		int i = 0;
		// Check for BOM
		if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
				&& (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
			i = 3;
		}

		int end;
		for (int j = input.length; i < j; ++i) {
			int octet = input[i];
			if ((octet & 0x80) == 0) {
				continue; // ASCII
			}

			// Check for UTF-8 leading byte
			if ((octet & 0xE0) == 0xC0) {
				end = i + 1;
			} else if ((octet & 0xF0) == 0xE0) {
				end = i + 2;
			} else if ((octet & 0xF8) == 0xF0) {
				end = i + 3;
			} else {
				// Java only supports BMP so 3 is max
				return false;
			}

			while (i < end) {
				i++;
				octet = input[i];
				if ((octet & 0xC0) != 0x80) {
					// Not a valid trailing byte
					return false;
				}
			}
		}
		return true;
	}

}
