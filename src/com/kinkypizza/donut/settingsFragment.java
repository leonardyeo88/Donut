package com.kinkypizza.donut;

import org.json.JSONException;
import org.json.JSONObject;

import library.SessionManager;
import library.UserFunctions;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class settingsFragment extends Fragment {
	
	private Button logoutButton, feedbackButton;
	private EditText feedbackText;
	Context myContext;
	SessionManager session;
	private Activity myActivity;
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_UNSUCCESSFULL = "unsuccessfull";
    String feedbackString, UID;
	int uID;
    
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings, null, false);
		myActivity = getActivity();
		session = new SessionManager(myActivity);
		
		logoutButton = (Button) v.findViewById(R.id.logout);
		feedbackButton = (Button) v.findViewById(R.id.submitButton);
		feedbackText = (EditText) v.findViewById(R.id.feedbackEditText);
		
		
		logoutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				session.logoutUser();
				Intent intent = new Intent(myActivity, MainActivity.class);
				startActivity(intent);
			}
        	
        });
		
		feedbackButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
           	 //String text = holder.textViewID;
            
           	 
           	 //Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
           	 new insertFeedback().execute();
            }
        });
		
	  return v;
	 }
	
	private class insertFeedback extends AsyncTask<String, String, JSONObject>{
		
		private ProgressDialog pDialog;

        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            

            feedbackString= feedbackText.getText().toString();
            uID = session.getUserDetails();
            UID = String.valueOf(uID);
            pDialog = new ProgressDialog(myActivity);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Sending Feedback ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

		@Override
		protected JSONObject doInBackground(String... arg) {
			UserFunctions userFunction = new UserFunctions();
	        JSONObject json = userFunction.addFeedback(UID, feedbackString);
	        return json;
		}
		
		@Override
        protected void onPostExecute(JSONObject json) {
    	super.onPostExecute(json);
    	pDialog.dismiss();
    	try {
            if (json.getString(KEY_SUCCESS) != null) {
                //registerErrorMsg.setText("");
                String res = json.getString(KEY_SUCCESS);
                String rei = json.getString(KEY_UNSUCCESSFULL);
                String red = json.getString(KEY_ERROR);

                if(Integer.parseInt(res) == 1){
                	Toast.makeText(getActivity(), "Feedback Sent", Toast.LENGTH_SHORT).show();
                	
                }

                else if (Integer.parseInt(red) ==1){
                    //pDialog.dismiss();
                    //registerErrorMsg.setText("User already exists");
                	Toast.makeText(getActivity(), "Not OK", Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(rei) ==1){
                    //pDialog.dismiss();
                    //registerErrorMsg.setText("Invalid Email id");
                	Toast.makeText(getActivity(), "Already Registered", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(red) == 1){
                	//pDialog.dismiss();
                    //registerErrorMsg.setText("Something is wrong");
                }

            }


                else{
               // pDialog.dismiss();

                    //registerErrorMsg.setText("Error occured in registration");
                }

        } catch (JSONException e) {
            e.printStackTrace();


        }
    	
    	
    	
	 }
		
	}

}
