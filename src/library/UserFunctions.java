package library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import library.JSONParser;

public class UserFunctions {
	private JSONParser jsonParser;
	
	 /*private static String registerURL = "http://10.0.2.2:8888/testing/register.php";
	 private static String loginURL = "http://10.0.2.2:8888/testing/login.php";
	 private static String volunteerEventsURL = "http://10.0.2.2:8888/testing/volunteerEvents.php";
	 private static String UserRegisterEventsURL = "http://10.0.2.2:8888/testing/UserRegisterEvents.php";*/
	
	 private static String registerURL = "http://vueartiste.com/donut/register.php";
	 private static String loginURL = "http://vueartiste.com/donut/login.php";
	 private static String volunteerEventsURL = "http://vueartiste.com/donut/volunteerEvents.php"; 
	 private static String UserRegisterEventsURL = "http://vueartiste.com/donut/UserRegisterEvents.php";
	 private static String UserFeedbackURL = "http://vueartiste.com/donut/addFeedback.php";
	
	
	 public UserFunctions(){
	        jsonParser = new JSONParser();
	    }
	 
	 public JSONObject registerUser(String email, String uname, String password, String school, String mobile){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        //params.add(new BasicNameValuePair("tag", register_tag));
	        //params.add(new BasicNameValuePair("fname", fname));
	        //params.add(new BasicNameValuePair("lname", lname));
	        params.add(new BasicNameValuePair("name", uname));
	        params.add(new BasicNameValuePair("email", email));
	        params.add(new BasicNameValuePair("password", password));
	        params.add(new BasicNameValuePair("school", school));
	        params.add(new BasicNameValuePair("mobile", mobile));
	        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
	        //JSONObject json = jsonParser.getJSONFromUrl(params);
	        return json;
	    }
	 
	 public JSONObject loginUser(String email, String password){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        //params.add(new BasicNameValuePair("tag", register_tag));
	        //params.add(new BasicNameValuePair("fname", fname));
	        //params.add(new BasicNameValuePair("lname", lname));
	        //params.add(new BasicNameValuePair("name", uname));
	       // params.add(new BasicNameValuePair("user_id", userID));
	       // params.add(new BasicNameValuePair("event_id", eventID));
	        params.add(new BasicNameValuePair("email", email));
	        params.add(new BasicNameValuePair("password", password));
	        JSONObject json = jsonParser.getJSONFromUrl(loginURL,params);
	        //JSONObject json = jsonParser.getJSONFromUrl(params);
	        return json;
	    }
	 
	 public JSONObject volunteerEvents(String userID, String eventID){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        //params.add(new BasicNameValuePair("tag", register_tag));
	        //params.add(new BasicNameValuePair("fname", fname));
	        //params.add(new BasicNameValuePair("lname", lname));
	        //params.add(new BasicNameValuePair("name", uname));
	        params.add(new BasicNameValuePair("user_id", userID));
	        params.add(new BasicNameValuePair("event_id", eventID));
	        JSONObject json = jsonParser.getJSONFromUrl(volunteerEventsURL,params);
	        //JSONObject json = jsonParser.getJSONFromUrl(params);
	        return json;
	    }

	public JSONObject checkUserRegisterEventsParse(String userID, String eventID) {
		  List<NameValuePair> params = new ArrayList<NameValuePair>();
	        //params.add(new BasicNameValuePair("tag", register_tag));
	        //params.add(new BasicNameValuePair("fname", fname));
	        //params.add(new BasicNameValuePair("lname", lname));
	        //params.add(new BasicNameValuePair("name", uname));
	        params.add(new BasicNameValuePair("user_id", userID));
	        params.add(new BasicNameValuePair("event_id", eventID));
	        JSONObject json = jsonParser.getJSONFromUrl(UserRegisterEventsURL,params);
	        //JSONObject json = jsonParser.getJSONFromUrl(params);
	        return json;
	}
	
	public JSONObject addFeedback(String userID, String feedback) {
		  List<NameValuePair> params = new ArrayList<NameValuePair>();
	        //params.add(new BasicNameValuePair("tag", register_tag));
	        //params.add(new BasicNameValuePair("fname", fname));
	        //params.add(new BasicNameValuePair("lname", lname));
	        //params.add(new BasicNameValuePair("name", uname));
	        params.add(new BasicNameValuePair("user_id", userID));
	        params.add(new BasicNameValuePair("feedback", feedback));
	        JSONObject json = jsonParser.getJSONFromUrl(UserFeedbackURL,params);
	        //JSONObject json = jsonParser.getJSONFromUrl(params);
	        return json;
	}
}
