package com.kinkypizza.donut;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import library.JSONParser;
import library.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class meFragment extends Fragment {
	
	private Activity myActivity;
	ExpandableListView exvU, exvC;
	TextView numOfEvents, numOfHours, userName, errormsg;
	Context context;
	
	JSONArray user = null;
	
	SessionManager session;
	
    
    private static String urlcompleted = "http://vueartiste.com/donut/checkCompleted.php";
    private static String urlupcoming = "http://vueartiste.com/donut/checkUpcoming.php";
    private static String urlrecord = "http://vueartiste.com/donut/userRecord.php";
	
	/*private static String urlcompleted = "http://10.0.2.2:8888/testing/checkCompleted.php";
    private static String urlupcoming = "http://10.0.2.2:8888/testing/checkUpcoming.php";
    private static String urlrecord = "http://10.0.2.2:8888/testing/userRecord.php";*/
    
	//JSON Node Names
	private static final String TAG_COMPLETED = "completed";
	private static final String TAG_UPCOMING = "upcoming";
	private static final String TAG_RECORD = "records";
	
	private static final String TAG_ID = "event_hours";
	private static final String TAG_NAME = "event_name";
	private static final String TAG_DATE = "event_date";
	private static final String TAG_EVENTS = "user_numevents";
	private static final String TAG_HOURS = "user_numhours";
	private static final String TAG_USERNAME = "user_name";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.fragment_me, null, false);
		
		
		myActivity=getActivity();
		session = new SessionManager(myActivity);
		
		exvU = (ExpandableListView) v.findViewById(R.id.expandableListViewUpcoming);
		exvU.setGroupIndicator(null);
		exvU.setDivider(null); 
		
		exvC = (ExpandableListView) v.findViewById(R.id.expandableListViewCompleted);
		exvC.setGroupIndicator(null);
		exvC.setDivider(null); 
		
		//new GetCompleted().execute();
		//new GetUpcoming().execute();
		
		numOfEvents = (TextView) v.findViewById(R.id.events);
		numOfHours = (TextView) v.findViewById(R.id.hours);
		userName = (TextView) v.findViewById(R.id.title);
		errormsg = (TextView) v.findViewById(R.id.errormsg);
		//new GetRecords().execute();
		
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
                	//URL url = new URL("http://vueartiste.com/donut/allEvents.php");
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
                new GetRecords().execute();
                new GetCompleted().execute();
        		new GetUpcoming().execute();
            }
            else{
                nDialog.dismiss();
                errormsg.setText("Error in Network Connection");
            }
        }
    }
	
	private class GetRecords extends AsyncTask<Void, Void, ArrayList<record>>{
		private ProgressDialog pDialog;
		@Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(myActivity);
            pDialog.setMessage("Loading my Information..");
            //pDialog.setTitle("Getting Events");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

		@Override
		protected ArrayList<record> doInBackground(Void... params) {
			
			int uID = session.getUserDetails();
			ArrayList<record> itemsrecord = new ArrayList<record>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(uID)));
			// Creating new JSON Parser
					JSONParser jParser = new JSONParser();
					// Getting JSON from URL
					JSONObject json = jParser.getJSONFromUrl(urlrecord, param);
					try {
									// Getting JSON Array
									user = json.getJSONArray(TAG_RECORD);
									
									
									for(int i=0; i<user.length(); i++){
										JSONObject c = user.getJSONObject(i);
										// Storing  JSON item in a Variable
										String name = c.getString(TAG_USERNAME);
										int events = c.getInt(TAG_EVENTS);
										int hours = c.getInt(TAG_HOURS);
										//String status = c.getString(TAG_STATUS);
										
										itemsrecord.add(new record(name, hours, events));
									}
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			
			//itemsrecord.add(1);
			//itemsrecord.add(1);
			return itemsrecord;
		}
		
		protected void onPostExecute(ArrayList<record> result){
			super.onPostExecute(result);
			pDialog.dismiss();
			//result = new ArrayList<record>();
			numOfEvents.setText(String.valueOf(result.get(0).getNumEvents()));
			numOfHours.setText(String.valueOf(result.get(0).getNumHours()));
			userName.setText(result.get(0).getName());
			
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Delicious-Heavy.ttf");
            
            numOfEvents.setTypeface(tf);
            numOfHours.setTypeface(tf);
            userName.setTypeface(tf);
		}
		
	}
	
	private class GetCompleted extends AsyncTask<Void, Void, ArrayList<completed>>{
		private ProgressDialog pDialog;
		 @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            pDialog = new ProgressDialog(myActivity);
	            pDialog.setMessage("Loading..");
	            //pDialog.setTitle("Getting Events");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

		@Override
		protected ArrayList<completed> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			
			int uID = session.getUserDetails();
			ArrayList<completed> itemscompleted = new ArrayList<completed>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(uID)));
			// Creating new JSON Parser
					JSONParser jParser = new JSONParser();
					// Getting JSON from URL
					JSONObject json = jParser.getJSONFromUrl(urlcompleted, param);
					try {
						user = json.getJSONArray(TAG_COMPLETED);
						
						
						for(int i=0; i<user.length(); i++){
							JSONObject c = user.getJSONObject(i);
							// Storing  JSON item in a Variable
							int id = c.getInt(TAG_ID);
							String name = c.getString(TAG_NAME);
							//String image = c.getString(TAG_IMAGE);
							//String status = c.getString(TAG_STATUS);
							
							itemscompleted.add(new completed(id, 
								name));
						}
		          
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			
			return itemscompleted;
		}
		
		protected void onPostExecute(ArrayList<completed> result){
			super.onPostExecute(result);
			pDialog.dismiss();
			exvC.setAdapter(new SavedTabsListAdapter(myActivity, exvC, result));
			
		}
		
	}
	
	
	private class GetUpcoming extends AsyncTask<Void, Void, ArrayList<upcoming>>{
		private ProgressDialog pDialog;
		 @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            pDialog = new ProgressDialog(myActivity);
	            pDialog.setMessage("Loading..");
	            //pDialog.setTitle("Getting Events");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

		@Override
		protected ArrayList<upcoming> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			
			int uID = session.getUserDetails();
			ArrayList<upcoming> itemscompleted = new ArrayList<upcoming>();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(uID)));
			// Creating new JSON Parser
					JSONParser jParser = new JSONParser();
					// Getting JSON from URL
					JSONObject json = jParser.getJSONFromUrl(urlupcoming, param);
					try {
						
						user = json.getJSONArray(TAG_UPCOMING);
						
						
						for(int i=0; i<user.length(); i++){
							JSONObject c = user.getJSONObject(i);
							// Storing  JSON item in a Variable
							//int id = c.getInt(TAG_ID);
							String name = c.getString(TAG_NAME);
							String date = c.getString(TAG_DATE);
							//String status = c.getString(TAG_STATUS);
							
							itemscompleted.add(new upcoming(name, 
								date));
						}
		           
				} catch (JSONException e) {
					e.printStackTrace();
				}
			
			return itemscompleted;
		}
		
		protected void onPostExecute(ArrayList<upcoming> result){
			super.onPostExecute(result);
			pDialog.dismiss();
			exvU.setAdapter(new SavedTabsListAdapter2(myActivity, exvU, result));
			
		}
		
	}

	
public class SavedTabsListAdapter2 extends BaseExpandableListAdapter{
		
		private Context context2;
		private ArrayList<upcoming> objectsupcoming;
		// TextView textViewID;
		TextView textViewTitle, textViewTitleGroup;
		
		 class ViewHolder {
			  TextView 
			  textViewTitle,
			  textViewDate;
			  
			  ImageView imageview;
			  
			  Button button;
			}
		
		
		
		
		public SavedTabsListAdapter2(Context context,ExpandableListView list, ArrayList<upcoming> objectsupcoming) {
            this.context2 = context;
            this.objectsupcoming = objectsupcoming;
        }
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			
			//return children[groupPosition][childPosition];
			upcoming[] setObjects = (upcoming[]) objectsupcoming.toArray();
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
			LayoutInflater inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.me_upcoming_layout, null);
            
            
            
            ViewHolder holder = new ViewHolder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.upcomingEventName);
			holder.textViewDate = (TextView) convertView.findViewById(R.id.upcomingEventDate);
			//textView.setHeight(200);
			//textView.setPadding(80,50,0,0);
			//textView.setTextSize(20);
			//holder.textViewTitle.setText(getChild(groupPosition, childPosition).toString());
			//holder.textViewID.setText(objects.get(groupPosition).getID());
			holder.textViewTitle.setText(String.valueOf(objectsupcoming.get(childPosition).getEventName()));
			holder.textViewDate.setText(objectsupcoming.get(childPosition).getEventDate());
			
            Typeface tg = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Delicious-Roman.ttf");
            
            holder.textViewDate.setTypeface(tg);
            holder.textViewTitle.setTypeface(tg);
			
			convertView.setTag(holder);
			return convertView;
			
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			//return children[groupPosition].length;
			//return 1;
			return objectsupcoming.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupPosition;
			// TODO Auto-generated method stub
			//return objects[groupPosition];
			//events[] setObjects = (events[]) objects.toArray();
			//completed[] setObjects = (completed[]) objectscompleted.toArray();
			//return setObjects[groupPosition];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			//return groups.length;
			
			return 1;
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
			LayoutInflater inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.me_group_layout_upcoming, null);
            
			textViewTitleGroup = (TextView) convertView.findViewById(R.id.title);
			
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Delicious-Heavy.ttf");
            
			textViewTitleGroup.setTypeface(tf);
			
			
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
			return false;
		}
		
	}
	
	
	
public class SavedTabsListAdapter extends BaseExpandableListAdapter{
		
		private Context context;
		private ArrayList<completed> objectscompleted;
		// TextView textViewID;
		TextView textViewTitle, textViewTitleGroup;
		
		 class ViewHolder {
			  TextView 
			  textViewTitle, 
			  textViewVenue,
			  textViewDate,
			  textViewStartTime, 
			  textViewEndTime, 
			  textViewPax, 
			  textViewDescription, 
			  textViewID;
			  
			  ImageView imageview;
			  
			  Button button;
			}
		
		
		
		
		public SavedTabsListAdapter(Context context,ExpandableListView list, ArrayList<completed> objectscompleted) {
            this.context = context;
            this.objectscompleted = objectscompleted;
        }
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			
			//return children[groupPosition][childPosition];
			completed[] setObjects = (completed[]) objectscompleted.toArray();
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
            convertView = inflater.inflate(R.layout.me_completed_layout, null);
            ViewHolder holder = new ViewHolder();
            holder.textViewID = (TextView) convertView.findViewById(R.id.completedEventHours);
			holder.textViewTitle = (TextView) convertView.findViewById(R.id.completedEventName);
			//textView.setHeight(200);
			//textView.setPadding(80,50,0,0);
			//textView.setTextSize(20);
			//holder.textViewTitle.setText(getChild(groupPosition, childPosition).toString());
			//holder.textViewID.setText(objects.get(groupPosition).getID());
			holder.textViewID.setText(String.valueOf(objectscompleted.get(groupPosition).getEventHours()));
			holder.textViewTitle.setText(objectscompleted.get(groupPosition).getEventDate());
			
            Typeface tg = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Delicious-Roman.ttf");
            
            holder.textViewID.setTypeface(tg);
            holder.textViewTitle.setTypeface(tg);
			
			convertView.setTag(holder);
			return convertView;
			
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			//return children[groupPosition].length;
			//return 1;
			return objectscompleted.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupPosition;
			// TODO Auto-generated method stub
			//return objects[groupPosition];
			//events[] setObjects = (events[]) objects.toArray();
			//completed[] setObjects = (completed[]) objectscompleted.toArray();
			//return setObjects[groupPosition];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			//return groups.length;
			
			return 1;
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
            convertView = inflater.inflate(R.layout.me_group_layout, null);
            
			textViewTitleGroup = (TextView) convertView.findViewById(R.id.title);
			
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Delicious-Heavy.ttf");
            
			textViewTitleGroup.setTypeface(tf);
			
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
	
	
	public void onStart() {
        super.onStart();
	}
	
	
}
