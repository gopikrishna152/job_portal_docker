package com.gopikrishna.jobportal.entity;

public interface IRecruiterJobs {
	
	Long getTotalCandidates(); 
	 
	Integer getJob_post_id();
	
	String getJob_title();
	Integer getLocationid();
	String getCity(); 
	String getState(); 
	String getCountry(); 
	Integer getComapnyId();
	
	String getName();
}
