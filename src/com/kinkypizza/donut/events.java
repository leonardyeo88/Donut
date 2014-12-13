package com.kinkypizza.donut;
public class events {
	
	private int ID;
 	private String title; 
 	private String venue; 
    private String description;
    private String date;
    private String starttime;
    private String endtime;
    private int pax;
    private String url;
    

    public events(int srtID, 
    		String srtTitle, 
    		String srtDescription,
    		String strVenue, 
    		String strDate, 
    		String strStartTime, 
    		String strEndTime, 
    		int intPax, 
    		String strUrl) {
    	ID = srtID;
    	title = srtTitle;
    	venue = strVenue;
    	description = srtDescription;
    	date = strDate;
    	starttime = strStartTime;
    	endtime = strEndTime;
    	pax = intPax;
    	url = strUrl;
    	
    }

    /*public events(String optString, String optString2, String optString3,
			String optString4, String optString5, String optString6,
			String optString7, String optString8) {
		// TODO Auto-generated constructor stub
    	ID = optString;
	}*/

	public int getID() {
        return ID;
    }

    public String getDescription() {
       return description;
    }
    
    public String getTitle() {
	       return title;
	    }
    
    public String getDate() {
	       return date;
	    }
    
    public String getStartTime() {
	       return starttime;
	    }
    
    public String getVenue() {
	       return venue;
	    }
    
    public String getEndTime() {
	       return endtime;
	    }
    
    public int getPax() {
	       return pax;
	    }
    
    public String getUrl() {
	       return url;
	    }
}
