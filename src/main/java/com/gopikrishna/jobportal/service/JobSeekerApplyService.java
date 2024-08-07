package com.gopikrishna.jobportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gopikrishna.jobportal.entity.JobPostActivity;
import com.gopikrishna.jobportal.entity.JobSeekerApply;
import com.gopikrishna.jobportal.entity.JobSeekerProfile;
import com.gopikrishna.jobportal.repository.JobSeekerApplyRepository;
@Service
public class JobSeekerApplyService {
	
	 private final JobSeekerApplyRepository jobSeekerApplyRepository;

	    @Autowired
	    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
	        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
	    }

	    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId) {
	        return jobSeekerApplyRepository.findByUserId(userAccountId);
	    }

	    public List<JobSeekerApply> getJobCandidates(JobPostActivity job) {
	        return jobSeekerApplyRepository.findByJob(job);
	    }

	    public void addNew(JobSeekerApply jobSeekerApply) {
	        jobSeekerApplyRepository.save(jobSeekerApply);
	    }
	
}
