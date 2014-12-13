package com.kinkypizza.donut;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import library.JSONParser;
import library.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


public class friendsFragment extends Fragment{
	
	
	private Activity myActivity;
	
	TextView errormsg;
	
	 
    // URL to get contacts JSON
    //private static String url = "http://10.0.2.2:8888/testing/";
	
	ExpandableListView exv;
	/*private String[] groups = {"BOOK DRIVE", 
			"FLAG DAY", 
			"JOG-A-THON"};*/
	/*private String[][] children = {
			{"21/12/2013\n Come come!"},{"01/01/2013\n over liao la!"},{"20/01/2014\n very soon!"}
	};*/
	
	//URL to get JSON Array
	private static String url = "http://vueartiste.com/donut/userFriends.php";
	//private static String url = "http://10.0.2.2:8888/testing/userFriends.php";
	//JSON Node Names
	private static final String TAG_USER = "friends";
	//private static final String TAG_ID = "user_name";
	private static final String TAG_USERNAME = "user_name";
	private static final String TAG_EVENTNAME = "event_name";
	private static final String TAG_EVENTDATE = "event_date";
	private static final String TAG_EVENTSTART = "event_start_time";
	private static final String TAG_EVENTEND = "event_end_time";
	//private static final String TAG_IMAGE = "event_image";
	//private static final String TAG_STATUS = "event_status";
	JSONArray user = null;
	
	SessionManager session;
	
	//private static String KEY_SUCCESS = "success";
    //private static String KEY_ERROR = "failed";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_friends, null, false);
		myActivity=getActivity();
		exv = (ExpandableListView) v.findViewById(R.id.expandableListViewFriends);
		exv.setGroupIndicator(null);
		exv.setDivider(null); 
		
		session = new SessionManager(myActivity);
        
        errormsg = (TextView) v.findViewById(R.id.errormsg);
		
		//ArrayList<friends> objects = new ArrayList<friends>();
		//new EventAsyncTask().execute();
		//objects.add(new friends("book drive", "hougang", "description la", "11 jan", "14:00"));
		//objects.add(new friends("book drive 2", "hougang 2", "description 2", "20 jan", "13:00"));
		//exv.setAdapter(new SavedTabsListAdapter2(myActivity, exv, objects));
		//new GetFriends().execute();
		new NetCheck().execute();
		
		return v;
	}
	
	private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(myActivity);
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
            ConnectivityManager cm = (ConnectivityManager) myActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

            if(th == true){
                nDialog.dismiss();
                new GetFriends().execute();
            }
            else{
                nDialog.dismiss();
                errormsg.setText("Error in Network Connection");
            }
        }
    }
	
	private class GetFriends extends AsyncTask<Void, Void, ArrayList<friends>>{
		private ProgressDialog pDialog;
		 @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            pDialog = new ProgressDialog(myActivity);
	            pDialog.setMessage("Loading Friends..");
	            //pDialog.setTitle("Getting Friends");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

		@Override
		protected ArrayList<friends> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			int uID = session.getUserDetails();
			ArrayList<friends> items = new ArrayList<friends>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(uID)));
			// Creating new JSON Parser
					JSONParser jParser = new JSONParser();
					// Getting JSON from URL
					JSONObject json = jParser.getJSONFromUrl(url, param);
					//JSONObject json = jParser.getJSONFromUrl(url);
					try {
						
						if(json != null){
						user = json.getJSONArray(TAG_USER);
						
						//items.add(new friends("book drive", "hougang", "description la", "11 jan", "14:00"));
						
						
						for(int i=0; i<user.length(); i++){
							JSONObject c = user.getJSONObject(i);
							// Storing  JSON item in a Variable
							//int id = c.getInt(TAG_ID);
							String name = c.getString(TAG_USERNAME);
							String description = c.getString(TAG_EVENTNAME);
							String venue = c.getString(TAG_EVENTDATE);
							String date = c.getString(TAG_EVENTSTART);
							String time = c.getString(TAG_EVENTEND);
							//String image = c.getString(TAG_IMAGE);
							//String status = c.getString(TAG_STATUS);
							
							
							
						items.add(new friends( 
								name, 
								description, 
								venue, 
								date,  
								time));
						}
						}else{
							return items;
						}
						
						//exv.setAdapter(new SavedTabsListAdapter(myActivity, exv, items));
			         
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			
			return items;
		}
		
		protected void onPostExecute(ArrayList<friends> result){
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result.size()>0){
			exv.setAdapter(new SavedTabsListAdapter2(myActivity, exv, result));
			}else{
				errormsg.setText("No friends... What are you waiting for? Go ask your friends to join Donut");
			}
			
		}
		
	}
	
	
	
	
	
	public class SavedTabsListAdapter2 extends BaseExpandableListAdapter{
		
		private Context context;
		private ArrayList<friends> objects;
		 TextView textViewID;
		
		 class ViewHolder {
			  TextView 
			  textViewTitle,
			  textViewDate,
			  textViewStartTime,
			  textViewEndTime,
			  textViewUserName;
			  
			  ImageView imageview;
			  
			  Button button;
			}
		
		
		
		
		public SavedTabsListAdapter2(Context context,ExpandableListView list, ArrayList<friends> objects) {
            this.context = context;
            this.objects = objects;
        }
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			
			//return children[groupPosition][childPosition];
			friends[] setObjects = (friends[]) objects.toArray();
			return setObjects[childPosition];
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 //View v = View.inflate(context, R.layout.child_view, null);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view, null);
			return convertView;
			
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			//return children[groupPosition].length;
			return 0;
			//return objects.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			//return objects[groupPosition];
			friends[] setObjects = (friends[]) objects.toArray();
			return setObjects[groupPosition];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			//return groups.length;
			
			return objects.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null, false);
            ViewHolder holder = new ViewHolder();
            //holder.textViewID = (TextView) convertView.findViewById(R.id.ID);
            //holder.imageview = (ImageView) convertView.findViewById(R.id.myImageView);
           // holder.imageview.getLayoutParams().height = 300;
            //holder.imageview.getDrawable().setColorFilter(getResources().getColor(R.color.translucent_black), PorterDuff.Mode.MULTIPLY);
            //holder.imageview.setBackgroundResource(R.color.translucent_black);
            
            //holder.imageview.setBackgroundColor(Color.rgb(100, 100, 50));
            Typeface tf = Typeface.createFromAsset(myActivity.getAssets(),
                    "fonts/Delicious-Heavy.ttf");
            Typeface tg = Typeface.createFromAsset(myActivity.getAssets(),
                    "fonts/Delicious-Roman.ttf");
            
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.title);
            holder.textViewTitle.setTypeface(tf);
            
            holder.textViewEndTime = (TextView) convertView.findViewById(R.id.hourText);
            holder.textViewEndTime.setTypeface(tg);
            
            holder.textViewDate = (TextView) convertView.findViewById(R.id.hours);
            holder.textViewDate.setTypeface(tg);
            
            holder.textViewStartTime = (TextView) convertView.findViewById(R.id.events);
            holder.textViewStartTime.setTypeface(tg);
            
            holder.textViewUserName = (TextView) convertView.findViewById(R.id.username);
            holder.textViewUserName.setTypeface(tg);
			
			
			holder.textViewTitle.setText(objects.get(groupPosition).getEvenTitle());
			holder.textViewStartTime.setText(objects.get(groupPosition).getEventStartTime());
			holder.textViewDate.setText(objects.get(groupPosition).getEventDate());
			holder.textViewEndTime.setText(objects.get(groupPosition).getEventEndTime());
			holder.textViewUserName.setText(objects.get(groupPosition).getName());
			
			convertView.setTag(holder);
			
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}

	
}
	
	
	
	
	
	
	
	
	
	
	
	

