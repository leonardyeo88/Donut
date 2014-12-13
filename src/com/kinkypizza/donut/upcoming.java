package com.kinkypizza.donut;

public class upcoming {

	private String eventName;
	private String eventDate;
	
	public upcoming(
			String streventName,
			String streventDate){
		eventName = streventName;
		eventDate = streventDate;
	}
	
	public String getEventName(){
		return eventName;
	}
	
	public String getEventDate(){
		return eventDate;
	}
	
}
