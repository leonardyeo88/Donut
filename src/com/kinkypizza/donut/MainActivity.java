package com.kinkypizza.donut;


import library.SessionManager;
import library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

	ActionBar actionBar;
	Context context;
	TextView registerErrorMsg;
	EditText emailEdit, passEdit;
	String email, password;
	Button loginButton, registerButton;
	JSONArray user = null;
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_USERID = "userID";
    
 // Session Manager Class
    SessionManager session;

	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        session = new SessionManager(getApplicationContext());
	        if(session.isLoggedIn() == true){
	        	Intent intent = new Intent(getApplicationContext(), naviController.class);
				startActivity(intent);
	        }else{
	        	setContentView(R.layout.activity_main);
	        	
	        	actionBar = getActionBar();
		        actionBar.setCustomView(R.layout.actionbar);
		        actionBar.setDisplayShowTitleEnabled(false);
		        actionBar.setDisplayShowCustomEnabled(true);
		        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		        
		        emailEdit = (EditText) findViewById(R.id.email);
		        passEdit = (EditText) findViewById(R.id.pword);
		        loginButton = (Button) findViewById(R.id.login);
		        registerErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
		        registerButton = (Button) findViewById(R.id.registerbtn);
		        
		        
		        
		        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
		        
		        loginButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if((!emailEdit.getText().toString().equals("")) && (!passEdit.getText().toString().equals(""))){
							new tryLogin().execute();
						}else{
							// 1. Instantiate an AlertDialog.Builder with its constructor
							Toast.makeText(getApplicationContext(), "Please enter your email and password",
								    Toast.LENGTH_SHORT).show();
						}
					}
		        	
		        });
		        
		        registerButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent registered = new Intent(getApplicationContext(), Register.class);

                        /**
                         * Close all views before launching Registered screen
                        **/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(registered);
					}
		        	
		        });
		        
		        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		        ImageAdapter adapter = new ImageAdapter(this);
		        viewPager.setAdapter(adapter);
	        }
	        
	        
	        
	        
	    }
	    
	    private class tryLogin extends AsyncTask<String, String, JSONObject>{
	    	
	    	private ProgressDialog pDialog;

	        
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            

	            email = emailEdit.getText().toString();
	            password = passEdit.getText().toString();
	            
	            pDialog = new ProgressDialog(MainActivity.this);
	            pDialog.setTitle("Contacting Servers");
	            pDialog.setMessage("Checking");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

			@Override
			protected JSONObject doInBackground(String... params) {
				 UserFunctions userFunction = new UserFunctions();
			        JSONObject json = userFunction.loginUser(email, password);

			            return json;
					
			}
			
			 @Override
		        protected void onPostExecute(JSONObject json) {
		    	super.onPostExecute(json);
		    	
		    	try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);

                        String red = json.getString(KEY_ERROR);

                        if(Integer.parseInt(res) == 1){
                        	pDialog.setTitle("Hello");
            	            pDialog.setMessage("Logging in ...");
                        	pDialog.dismiss();
                        	//registerErrorMsg.setText("Successfully logged in");
                            //pDialog.setTitle("Getting Data");
                            //pDialog.setMessage("Loading Info");

                        	 
                        	 JSONObject json_user = json.getJSONObject("user");
                        	 int uID = json_user.getInt(KEY_USERID);
                        	 session.createLoginSession(uID);


                           // DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            

                            /**
                             * Removes all the previous data in the SQlite database
                             **/

                            //UserFunctions logout = new UserFunctions();
                            //logout.logoutUser(getApplicationContext());
                            
                            /**
                             * Stores registered data in SQlite Database
                             * Launch Registered screen
                             **/

                            Intent login = new Intent(getApplicationContext(), naviController.class);

                            /**
                             * Close all views before launching Registered screen
                            **/
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //pDialog.dismiss();
                            startActivity(login);
                            //registerErrorMsg.setText("Successfully logged in");

                              finish();
                        }

                        else if (Integer.parseInt(red) ==2){
                            pDialog.dismiss();
                            registerErrorMsg.setText("User already exists");
                        }
                        else if (Integer.parseInt(red) ==3){
                            pDialog.dismiss();
                            registerErrorMsg.setText("Invalid Email id");
                        }
                        else if(Integer.parseInt(red) == 1){
                        	pDialog.dismiss();
                            registerErrorMsg.setText("Please enter the correct email and password");
                        }

                    }


                        else{
                        pDialog.dismiss();

                            registerErrorMsg.setText("Error occured in registration");
                        }

                } catch (JSONException e) {
                    e.printStackTrace();


                }
		    	
		    	
		    	
			 }

	    	
	    }

}

