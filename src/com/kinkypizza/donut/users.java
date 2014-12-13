package com.kinkypizza.donut;

public class users {
	
	private int ID;
	private String name;
	private String email;
	private String password;
	private String mobile;
	private String school;
	private String role;
	private String status;
	
	public users(int intID, 
			String strName,
			String strEmail, 
			String strPassword, 
			String strMobile, 
			String strSchool, 
			String strRole, 
			String strStatus){
		ID = intID;
		name = strName;
		email = strEmail;
		password = strPassword;
		mobile = strMobile;
		school = strSchool;
		role = strRole;
		status = strStatus;
	}
	
	public users(int intID){
		ID = intID;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getMobile(){
		return mobile;
	}
	
	public String getSchool(){
		return school;
	}
	
	public String getRole(){
		return role;
	}
	
	public String getStatus(){
		return status;
	}

}
