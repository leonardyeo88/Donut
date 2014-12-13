package com.kinkypizza.donut;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import library.JSONParser;
import library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Register extends Activity {


    /**
     *  JSON Response node names.
     **/


    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    /**
     * Defining layout items.
     **/
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputMobile;
    Button btnRegister, btnBack;
    TextView registerErrorMsg;
    String email,password,uname, spinnerobj, mobile;
    
    private static String url = "http://vueartiste.com/donut/getSchool.php";
    //private static String url = "http://10.0.2.2:8888/testing/getSchool.php";
    private static final String TAG_USER = "school";
	private static final String TAG_NAME = "school_name";
	
	JSONArray user = null;
    
    ActionBar actionBar;

    private Spinner spinner1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.register);
       
       actionBar = getActionBar();
       actionBar.setCustomView(R.layout.actionbar);
       actionBar.setDisplayShowTitleEnabled(false);
       actionBar.setDisplayShowCustomEnabled(true);

    /**
     * Defining all layout items
     **/
        //inputFirstName = (EditText) findViewById(R.id.fname);
        //inputLastName = (EditText) findViewById(R.id.lname);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        inputMobile = (EditText) findViewById(R.id.mobile);
        btnRegister = (Button) findViewById(R.id.register);
        btnBack = (Button) findViewById(R.id.bktologin);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        
        
        
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        /*List<String> list = new ArrayList<String>();
        list.add("RJ");
        list.add("HCI");
        list.add("RI");
        list.add("RGS");
         
        */
         
        // Spinner item selection Listener  
        //addListenerOnSpinnerItemSelection();
        new NetCheck2().execute();
       


/**
 * Button which Switches back to the login screen on clicked
 **/


        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            	Intent back = new Intent(getApplicationContext(), MainActivity.class);

                /**
                 * Close all views before launching Registered screen
                **/
            	back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
            	
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (( !inputUsername.getText().toString().equals("")) 
                		&& ( !inputPassword.getText().toString().equals("")) 
                		&& ( !inputEmail.getText().toString().equals(""))
                		&& ( !inputMobile.getText().toString().equals("")) 
                		&& ( !spinner1.getSelectedItem().toString().equals("")))
                {
                    //if ( inputUsername.getText().toString().length() > 4 ){
                    //NetAsync(view);
                    	//new ProcessRegister().execute();
                	new NetCheck().execute();
                    //}
                    //else
                    //{
                      //  Toast.makeText(getApplicationContext(),
                       //         "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    //}
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
       }
    
    
    private class NetCheck2 extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Checking Network..");
            //nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                	//URL url = new URL("http://10.0.2.2:8888/testing/allEvents.php");
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){
        	//pullToRefreshView.onRefreshComplete();
            if(th == true){
                nDialog.dismiss();
                new GetSchool().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }
	
	
	
	private class GetSchool extends AsyncTask<Void, Void, ArrayList<String>>{
		private ProgressDialog pDialog;
		 @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Register.this);
	            pDialog.setMessage("Loading Events..");
	            //pDialog.setTitle("Getting Events");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			ArrayList<String> items = new ArrayList<String>();
			// Creating new JSON Parser
					JSONParser jParser = new JSONParser();
					// Getting JSON from URL
					JSONObject json = jParser.getJSONFromUrl(url);
					try {
						// Getting JSON Array
						user = json.getJSONArray(TAG_USER);
						
						
						for(int i=0; i<user.length(); i++){
							JSONObject c = user.getJSONObject(i);
							// Storing  JSON item in a Variable
							String name = c.getString(TAG_NAME);
							//String image = c.getString(TAG_IMAGE);
							//String status = c.getString(TAG_STATUS);
							
						items.add(name);
						}
						
						//exv.setAdapter(new SavedTabsListAdapter(myActivity, exv, items));
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			
			return items;
		}
		
		protected void onPostExecute(ArrayList<String> result){
			//pullToRefreshView.onRefreshComplete();
			super.onPostExecute(result);
			pDialog.dismiss();
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
            (Register.this, android.R.layout.simple_spinner_item, result);
             
			dataAdapter.setDropDownViewResource
			            (android.R.layout.simple_spinner_dropdown_item);
			             
			spinner1.setAdapter(dataAdapter);
			
		}
		
	}
    
    
    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                	//URL url = new URL("http://10.0.2.2:8888/testing/allEvents.php");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }





    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

/**
 * Defining Process dialog
 **/
        private ProgressDialog pDialog;

        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           //inputUsername = (EditText) findViewById(R.id.uname);
           //inputPassword = (EditText) findViewById(R.id.pword);
           //inputEmail = (EditText) findViewById(R.id.email);
          //     fname = inputFirstName.getText().toString();
          //     lname = inputLastName.getText().toString();
                //email = inputEmail.getText().toString();
                //uname= inputUsername.getText().toString();
                //password = inputPassword.getText().toString();
            

            email = inputEmail.getText().toString();
            uname= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            spinnerobj = String.valueOf(spinner1.getSelectedItem());
            mobile = inputMobile.getText().toString();
            
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.registerUser(email, uname, password, spinnerobj, mobile);

            return json;


        }
       @Override
        protected void onPostExecute(JSONObject json) {
       /**
        * Checks for success message.
        **
        *
       */
    	   super.onPostExecute(json);
    	   pDialog.dismiss();
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);

                        String red = json.getString(KEY_ERROR);

	                        if(Integer.parseInt(res) == 1){
	                            pDialog.setTitle("Getting Data");
	                            pDialog.setMessage("Loading Info");
	
	                            registerErrorMsg.setText("Successfully Registered");
	
	                            Intent registered = new Intent(getApplicationContext(), MainActivity.class);
	
	                            /**
	                             * Close all views before launching Registered screen
	                            **/
	                            registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                            
	                            startActivity(registered);
	
	
	                              finish();
	                        }else if (Integer.parseInt(res) ==0){
	                            registerErrorMsg.setText("User already exists. Please enter a different email");

	                        }else if(Integer.parseInt(red) == 1){
	                            registerErrorMsg.setText("Something is wrong");
	                        }

                    }


                        else{
                            registerErrorMsg.setText("Error occured in registration");
                        }

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }}
        public void NetAsync(View view){
            new NetCheck().execute();
        }}


