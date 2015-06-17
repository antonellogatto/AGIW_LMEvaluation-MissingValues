package Bing_Test.main;

import java.io.IOException;

import org.json.JSONException;

public class ExecuteQuery {

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		ExecuteQueryFirst first = new ExecuteQueryFirst();

		ExecuteQuerySecond second = new ExecuteQuerySecond();
		
//		try {
//			first.execute();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			second.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
