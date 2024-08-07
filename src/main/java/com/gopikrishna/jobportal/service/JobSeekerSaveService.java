package com.gopikrishna.jobportal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gopikrishna.jobportal.entity.JobPostActivity;
import com.gopikrishna.jobportal.entity.JobSeekerProfile;
import com.gopikrishna.jobportal.entity.JobSeekerSave;
import com.gopikrishna.jobportal.repository.JobSeekerSaveRepository;

@Service
public class JobSeekerSaveService {

	
	 private final JobSeekerSaveRepository jobSeekerSaveRepository;

	    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
	        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
	    }

	    public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId) {
	        return jobSeekerSaveRepository.findByUserId(userAccountId);
	    }

	    public List<JobSeekerSave> getJobCandidates(JobPostActivity job) {
	        return jobSeekerSaveRepository.findByJob(job);
	    }

	    public void addNew(JobSeekerSave jobSeekerSave) {
	        jobSeekerSaveRepository.save(jobSeekerSave);
	    }
}
