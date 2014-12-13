package com.kinkypizza.donut;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import library.JSONParser;
import library.PullToRefreshExpandableListView;
import library.SessionManager;
import library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class eventsFragment extends Fragment{
	
	
	private Activity myActivity;
	Context context;
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
		/*private static String url = "http://10.0.2.2:8888/testing/index.php";
		//JSON Node Names
		private static final String TAG_USER = "event";
		private static final String TAG_ID = "event_id";
		private static final String TAG_NAME = "event_name";
		private static final String TAG_DESCRIPTION = "event_description";
		private static final String TAG_VENUE = "event_venue";
		private static final String TAG_DATE = "event_date";
		private static final String TAG_TIME = "event_time";
		private static final String TAG_IMAGE = "event_image";
		private static final String TAG_STATUS = "event_status";
		JSONArray user = null;*/
	
	//private static String url = "http://10.0.2.2:8888/testing/allEvents.php";
	private static String url = "http://vueartiste.com/donut/allEvents.php";
	//JSON Node Names
	private static final String TAG_USER = "event";
	private static final String TAG_ID = "event_id";
	private static final String TAG_NAME = "event_name";
	private static final String TAG_DESCRIPTION = "event_description";
	private static final String TAG_VENUE = "event_venue";
	private static final String TAG_DATE = "event_date";
	private static final String TAG_STARTTIME = "event_start_time";
	private static final String TAG_ENDTIME = "event_end_time";
	private static final String TAG_PAX = "event_pax";
	private static final String TAG_URL = "org_url";
	//private static final String TAG_IMAGE = "event_image";
	//private static final String TAG_STATUS = "event_status";
	JSONArray user = null;
	
	SessionManager session;
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_UNSUCCESSFULL = "unsuccessfull";
    
    PullToRefreshExpandableListView pullToRefreshView;
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_events, container, false);
		myActivity=getActivity();
		exv = (ExpandableListView) v.findViewById(R.id.expandableListView1);
		exv.setGroupIndicator(null);
		exv.setDivider(null); 
		
		session = new SessionManager(myActivity);
        
        errormsg = (TextView) v.findViewById(R.id.errormsg);
		//ArrayList<events> objects = new ArrayList<events>();
		//new EventAsyncTask().execute();
		//objects.add(new events(0, "book drive", "hougang", "description la", "11 jan", "14:00"));
		//objects.add(new events(1, "book drive 2", "hougang 2", "description 2", "20 jan", "13:00"));
		//exv.setAdapter(new SavedTabsListAdapter(myActivity, exv, objects));
		//new GetEvents().execute();
        
        new NetCheck().execute();
        
        /*pullToRefreshView = (PullToRefreshExpandableListView) v.findViewById(R.id.pulltorefresh);
        pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                // Do work to refresh the list here.
            	new NetCheck().execute();
            }
        });*/
		//new NetCheck().execute();
		
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
        	//pullToRefreshView.onRefreshComplete();
            if(th == true){
                nDialog.dismiss();
                new GetEvents().execute();
            }
            else{
                nDialog.dismiss();
                errormsg.setText("Error in Network Connection");
            }
        }
    }
	
	
	
	private class GetEvents extends AsyncTask<Void, Void, ArrayList<events>>{
		private ProgressDialog pDialog;
		 @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            pDialog = new ProgressDialog(myActivity);
	            pDialog.setMessage("Loading Events..");
	            //pDialog.setTitle("Getting Events");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

		@Override
		protected ArrayList<events> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			ArrayList<events> items = new ArrayList<events>();
					// Creating new JSON Parser
					JSONParser jParser = new JSONParser();
					// Getting JSON from URL
					JSONObject json = jParser.getJSONFromUrl(url);
					try {
						
						if(json != null){
						// Getting JSON Array
						user = json.getJSONArray(TAG_USER);
						
							for(int i=0; i<user.length(); i++){
								JSONObject c = user.getJSONObject(i);
								// Storing  JSON item in a Variable
								int id = c.getInt(TAG_ID);
								String name = c.getString(TAG_NAME);
								String description = c.getString(TAG_DESCRIPTION);
								String venue = c.getString(TAG_VENUE);
								String date = c.getString(TAG_DATE);
								String starttime = c.getString(TAG_STARTTIME);
								String endTime = c.getString(TAG_ENDTIME);
								int pax = c.getInt(TAG_PAX);
								String url = c.getString(TAG_URL);
								//String image = c.getString(TAG_IMAGE);
								//String status = c.getString(TAG_STATUS);
								
							items.add(new events(id, 
									name, 
									description, 
									venue, 
									date,  
									starttime, 
									endTime, 
									pax, 
									url));
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
		
		protected void onPostExecute(ArrayList<events> result){
			//pullToRefreshView.onRefreshComplete();
			super.onPostExecute(result);
			pDialog.dismiss();
			//exv.setAdapter(new SavedTabsListAdapter(myActivity, exv, result));
			//exv = pullToRefreshView.getRefreshableView();
			if(result.size()>0){
			exv.setAdapter(new SavedTabsListAdapter(myActivity, exv, result));
			}else{
				errormsg.setText("No events currently. We are bringing the coolest events to you guys soon. Stay tuned...");
			}
		}
		
	}
	
	
	
	
	
	public class SavedTabsListAdapter extends BaseExpandableListAdapter{
		
		private Context context;
		private ArrayList<events> objects;
		 //TextView textViewID;
		
		 class ViewHolder {
			  TextView 
			  textViewTitle,
			  textViewVenue,
			  textViewDate,
			  textViewStartTime, 
			  textViewEndTime, 
			  textViewPax, 
			  textViewDescription, 
			  textViewURL;
			  
			  ImageView imageview;
			  
			  Button button;
			}
		
		
		
		
		public SavedTabsListAdapter(Context context,ExpandableListView list, ArrayList<events> objects) {
            this.context = context;
            this.objects = objects;
        }
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			
			//return children[groupPosition][childPosition];
			events[] setObjects = (events[]) objects.toArray();
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
            ViewHolder holder = new ViewHolder();
            //textViewID = (TextView) convertView.findViewById(R.id.ID);
			holder.textViewTitle = (TextView) convertView.findViewById(R.id.title);
			holder.textViewVenue = (TextView) convertView.findViewById(R.id.venue);
			holder.textViewDate = (TextView) convertView.findViewById(R.id.hours);
			holder.textViewStartTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.textViewEndTime = (TextView) convertView.findViewById(R.id.endTime);
			holder.textViewDescription = (TextView) convertView.findViewById(R.id.description);
			holder.textViewPax = (TextView) convertView.findViewById(R.id.pax);
			holder.textViewURL = (TextView) convertView.findViewById(R.id.organiser);
			holder.button = (Button) convertView.findViewById(R.id.submit);
			//textView.setHeight(200);
			//textView.setPadding(80,50,0,0);
			//textView.setTextSize(20);
			//holder.textViewTitle.setText(getChild(groupPosition, childPosition).toString());
			//holder.textViewID.setText(objects.get(groupPosition).getID());
			final String getID = String.valueOf(objects.get(groupPosition).getID());
			//textViewID.setText(getID);
			
			//holder.textViewTitle.setText(objects.get(childPosition).getTitle());
			
			holder.textViewTitle.setText(objects.get(groupPosition).getTitle());
			
			holder.textViewVenue.setText(objects.get(groupPosition).getVenue());
			holder.textViewDate.setText(objects.get(groupPosition).getDate());
			holder.textViewStartTime.setText(objects.get(groupPosition).getStartTime());
			holder.textViewEndTime.setText(objects.get(groupPosition).getEndTime());
			holder.textViewDescription.setText(objects.get(groupPosition).getDescription());
			holder.textViewPax.setText(String.valueOf(objects.get(groupPosition).getPax()));
			holder.textViewURL.setClickable(true);
			holder.textViewURL.setMovementMethod(LinkMovementMethod.getInstance());
			String text = "<a href='"+objects.get(groupPosition).getUrl()+"'>"+ objects.get(groupPosition).getUrl() +"</a>";
			holder.textViewURL.setText(Html.fromHtml(text));
			
			//new checkUserRegisterEvents().execute(String.valueOf(session.getUserDetails()), getID);
			
			holder.button.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 //String text = holder.textViewID;
	            	 int uID = session.getUserDetails();
	            	 //Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
	            	 new insertEvents().execute(String.valueOf(uID), getID);
	             }
	         });
			
			
			convertView.setTag(holder);
			return convertView;
			
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			//return children[groupPosition].length;
			return 1;
			//return objects.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			//return objects[groupPosition];
			events[] setObjects = (events[]) objects.toArray();
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
            convertView = inflater.inflate(R.layout.group_view, null, false);
            ViewHolder holder = new ViewHolder();
            //holder.textViewID = (TextView) convertView.findViewById(R.id.ID);
            holder.imageview = (ImageView) convertView.findViewById(R.id.myImageView);
           // holder.imageview.getLayoutParams().height = 300;
            //holder.imageview.getDrawable().setColorFilter(getResources().getColor(R.color.translucent_black), PorterDuff.Mode.MULTIPLY);
            //holder.imageview.setBackgroundResource(R.color.translucent_black);
            
            //holder.imageview.setBackgroundColor(Color.rgb(100, 100, 50));
			holder.textViewTitle = (TextView) convertView.findViewById(R.id.title);
			holder.textViewVenue = (TextView) convertView.findViewById(R.id.venue);
			holder.textViewVenue.setVisibility(View.GONE);
			holder.textViewDate = (TextView) convertView.findViewById(R.id.hours);
			holder.textViewStartTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.textViewEndTime = (TextView) convertView.findViewById(R.id.endTime);
			
			holder.textViewTitle.setText(objects.get(groupPosition).getTitle());
			holder.textViewVenue.setText(objects.get(groupPosition).getVenue());
			holder.textViewDate.setText(objects.get(groupPosition).getDate());
			holder.textViewStartTime.setText(objects.get(groupPosition).getStartTime());
			holder.textViewEndTime.setText(objects.get(groupPosition).getEndTime());
			
			
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
	
	private class insertEvents extends AsyncTask<String, String, JSONObject>{

		@Override
		protected JSONObject doInBackground(String... arg) {
			UserFunctions userFunction = new UserFunctions();
	        JSONObject json = userFunction.volunteerEvents(arg[0], arg[1]);
	        return json;
		}
		
		@Override
        protected void onPostExecute(JSONObject json) {
    	super.onPostExecute(json);
    	
    	try {
            if (json.getString(KEY_SUCCESS) != null) {
                //registerErrorMsg.setText("");
                String res = json.getString(KEY_SUCCESS);
                String rei = json.getString(KEY_UNSUCCESSFULL);
                String red = json.getString(KEY_ERROR);

                if(Integer.parseInt(res) == 1){
                	Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                	
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
		
		/*private class checkUserRegisterEvents extends AsyncTask<String, String, Boolean>{

			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				return null;
			}

			
		}*/
		
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	

