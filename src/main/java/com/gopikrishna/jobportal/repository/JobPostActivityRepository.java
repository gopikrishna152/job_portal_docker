package com.gopikrishna.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gopikrishna.jobportal.entity.IRecruiterJobs;
import com.gopikrishna.jobportal.entity.JobPostActivity;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface JobPostActivityRepository extends JpaRepository<JobPostActivity,Integer>{
	
//	Basically what we're doing is that we're making use of a query that's going to retrieve a list of jobs
//
//	for a given recruiter ID basically saying,
//
//	 what jobs did we post?
//
//	I need to get a list of all of those jobs.
//	Then we'll also make some combinations or some joins and we'll join the job post activity, the job location and the company.
//
//	So basically grabbing all these different components here
//
//	of a job post that we've created.
//
//	We'll also include the count of the job seekers

//	or candidates who actually applied for that given job.
	 @Query(value = " SELECT COUNT(s.user_id) as totalCandidates,j.job_post_id,j.job_title,l.id as locationId,l.city,l.state,l.country,c.id as companyId,c.name FROM job_post_activity j " +
	            " inner join job_location l " +
	            " on j.job_location_id = l.id " +
	            " INNER join job_company c  " +
	            " on j.job_company_id = c.id " +
	            " left join job_seeker_apply s " +
	            " on s.job = j.job_post_id " +
	            " where j.posted_by_id = :recruiter " +
	            " GROUP By j.job_post_id" ,nativeQuery = true)
	    List<IRecruiterJobs> getRecruiterJobs(@Param("recruiter") int recruiter);

	    @Query(value = "SELECT * FROM job_post_activity j INNER JOIN job_location l on j.job_location_id=l.id  WHERE j" +
	            ".job_title LIKE %:job%"
	            + " AND (l.city LIKE %:location%"
	            + " OR l.country LIKE %:location%"
	            + " OR l.state LIKE %:location%) " +
	            " AND (j.job_type IN(:type)) " +
	            " AND (j.remote IN(:remote)) ", nativeQuery = true)
	    List<JobPostActivity> searchWithoutDate(@Param("job") String job,
	                                            @Param("location") String location,
	                                            @Param("remote") List<String> remote,
	                                            @Param("type") List<String> type);

	    @Query(value = "SELECT * FROM job_post_activity j INNER JOIN job_location l on j.job_location_id=l.id  WHERE j" +
	            ".job_title LIKE %:job%"
	            + " AND (l.city LIKE %:location%"
	            + " OR l.country LIKE %:location%"
	            + " OR l.state LIKE %:location%) " +
	            " AND (j.job_type IN(:type)) " +
	            " AND (j.remote IN(:remote)) " +
	            " AND (posted_date >= :date)", nativeQuery = true)
	    List<JobPostActivity> search(@Param("job") String job,
	                                 @Param("location") String location,
	                                 @Param("remote") List<String> remote,
	                                 @Param("type") List<String> type,
	                                 @Param("date") LocalDate searchDate);
	
}
