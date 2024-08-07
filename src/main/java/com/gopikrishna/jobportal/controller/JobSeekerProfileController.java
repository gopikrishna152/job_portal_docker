package com.gopikrishna.jobportal.controller;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gopikrishna.jobportal.entity.JobSeekerProfile;
import com.gopikrishna.jobportal.entity.Skills;
import com.gopikrishna.jobportal.entity.Users;
import com.gopikrishna.jobportal.repository.UsersRepository;
import com.gopikrishna.jobportal.service.JobSeekerProfileService;
import com.gopikrishna.jobportal.util.FileDownloadUtil;
import com.gopikrishna.jobportal.util.FileUploadUtil;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {
	private  JobSeekerProfileService jobSeekerPrrofileService; 
	private UsersRepository usersRepository;
	public JobSeekerProfileController(JobSeekerProfileService jobSeekerPrrofileService,
			UsersRepository usersRepository) {
	
		this.jobSeekerPrrofileService = jobSeekerPrrofileService;
		this.usersRepository = usersRepository;
	}
	
	@GetMapping("/")
	public String JobSeekerProfile(Model model) {
		System.out.println("1st");
		JobSeekerProfile jobSeekerprofile =new JobSeekerProfile();
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		System.out.println("2st");
		List<Skills> skills=new ArrayList<>(); 
		System.out.println("3st");
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername=authentication.getName();
			Users users=  usersRepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("Could not found User"));
			System.out.println("4st");
			Optional<JobSeekerProfile> seekerProfile=jobSeekerPrrofileService.getOne(users.getUserId());
		    if(seekerProfile.isPresent()) {
		    	System.out.println("4st");
		    	jobSeekerprofile=seekerProfile.get();
		    	if(jobSeekerprofile.getSkills().isEmpty()) {
		    		System.out.println("5st");
		    		skills.add(new Skills());
		    		jobSeekerprofile.setSkills(skills);
		    	}
		    } 
//		    System.out.println("6st");
		    model.addAttribute("skills", skills);
//		    System.out.println("7st");
		    model.addAttribute("profile",jobSeekerprofile);
//		    System.out.println("8st");
		}
		
		
//		System.out.println("9st");
		return "job-seeker-profile";
	}
	@PostMapping("/addNew")
	public String addNew(JobSeekerProfile jobSeekerProfile,@RequestParam("image")MultipartFile image,
						@RequestParam("pdf")MultipartFile pdf,
							Model model) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername=authentication.getName();
			Users users=  usersRepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("Could not found User"));
			jobSeekerProfile.setUserId(users);
			jobSeekerProfile.setUserAccountId(users.getUserId());
		}
			
		List<Skills> skillsList=new ArrayList<>();
		model.addAttribute("profile", jobSeekerProfile);
		model.addAttribute("skills", skillsList);
		
		for(Skills skills:jobSeekerProfile.getSkills()) {
			skills.setJobSeekerProfile(jobSeekerProfile);
		}
		String imageName="";
		String resumeName=""; 
		if(!Objects.equals(image.getOriginalFilename(),"")) {
			imageName=StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
			jobSeekerProfile.setProfilePhoto(imageName);
		}
		if(!Objects.equals(pdf.getOriginalFilename(),"")) {
			resumeName=StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
			jobSeekerProfile.setProfilePhoto(resumeName);
		}
		
		JobSeekerProfile SeekerProfile= jobSeekerPrrofileService.addNew(jobSeekerProfile);
		try {
			
			String uploadDir="photos/candidate/"+jobSeekerProfile.getUserAccountId();
			if(!Objects.equals(image.getOriginalFilename(), "")) {
				FileUploadUtil.saveFile(uploadDir,imageName,image);
			}
			if(!Objects.equals(pdf.getOriginalFilename(), "")) {
				FileUploadUtil.saveFile(uploadDir,resumeName,pdf);
			}
		}catch (IOException ex) {
			throw new RuntimeException(ex);
			// TODO: handle exception
		}
		
		return "redirect:/dashboard/";
	}
	
	@GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model) {

        Optional<JobSeekerProfile> seekerProfile = jobSeekerPrrofileService.getOne(id);
        model.addAttribute("profile", seekerProfile.get());
        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "userID") String userId) {

        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource = downloadUtil.getFileAsResourse("photos/candidate/" + userId, fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);

    }
	
	
	
}
