package com.kinkypizza.donut;

public class friends {
	
	//private int ID;
	private String name;
	private String eventTitle;
	private String eventDate;
	private String eventStartTime;
	private String eventEndTime;
	
	public friends( 
			String strName,
			String strEventTitle, 
			String strEventDate, 
			String strEventStartTime, 
			String strEventEndTime){
		name = strName;
		eventTitle = strEventTitle;
		eventDate = strEventDate;
		eventStartTime = strEventStartTime;
		eventEndTime = strEventEndTime;
	}
	
	
	public String getName(){
		return name;
	}
	
	public String getEvenTitle(){
		return eventTitle;
	}
	
	public String getEventDate(){
		return eventDate;
	}
	
	public String getEventStartTime(){
		return eventStartTime;
	}
	
	public String getEventEndTime(){
		return eventEndTime;
	}
}

