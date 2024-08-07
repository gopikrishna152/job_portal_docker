package com.gopikrishna.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gopikrishna.jobportal.entity.JobSeekerSave;
import java.util.List;

import com.gopikrishna.jobportal.entity.JobPostActivity;
import com.gopikrishna.jobportal.entity.JobSeekerProfile;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave,Integer> {
	
	 public List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);

	    List<JobSeekerSave> findByJob(JobPostActivity job);
}
