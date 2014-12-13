package com.kinkypizza.donut;

public class record {
	
	private String name;
 	private int numHours; 
 	private int numEvents;
    

    public record(
    		String strname, 
    		int intnumHours, 
    		int intnumEvents) {
    	name = strname;
    	numHours = intnumHours;
    	numEvents = intnumEvents;
    	
    }

	public int getNumHours() {
        return numHours;
    }

    public int getNumEvents() {
       return numEvents;
    }
    
    public String getName() {
	       return name;
	    }
    

}
