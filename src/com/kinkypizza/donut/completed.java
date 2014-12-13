package com.kinkypizza.donut;

public class completed {
	
	private int eventHours;
	private String eventDate;
	
	public completed(
			int inteventHours,
			String streventDate){
		eventHours = inteventHours;
		eventDate = streventDate;
	}
	
	public int getEventHours(){
		return eventHours;
	}
	
	public String getEventDate(){
		return eventDate;
	}
	

}
