package com.gopikrishna.jobportal.controller;

import java.util.Objects;
import java.util.Optional;

import org.apache.catalina.User;
import org.apache.catalina.util.StringUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.gopikrishna.jobportal.entity.RecruiterProfile;
import com.gopikrishna.jobportal.entity.Users;
import com.gopikrishna.jobportal.repository.UsersRepository;
import com.gopikrishna.jobportal.service.RecruiterProfileService;
import com.gopikrishna.jobportal.util.FileUploadUtil;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {
	private final UsersRepository Usersrepository;
	private final RecruiterProfileService recruiterProfileService;
	
	
	public RecruiterProfileController(UsersRepository repository,RecruiterProfileService recruiterProfileService) {
		
		this.Usersrepository = repository;
		this.recruiterProfileService =recruiterProfileService;
	}



	@GetMapping("/")
	public String recruiterProfile(Model model) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername=authentication.getName();
			Users users=  Usersrepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("Could not found User"));
			
		Optional<RecruiterProfile> recruiterProfile=	recruiterProfileService.getOne(users.getUserId());
			if(!recruiterProfile.isEmpty()) {
				model.addAttribute("profile",recruiterProfile.get());
			}
		
		
		
		}
		return "recruiter_profile";
		
	}
	
	
	
	@PostMapping("/addNew")
	public String addnew(RecruiterProfile recruiterProfile,@RequestParam("image") MultipartFile multipartfile,Model model) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername=authentication.getName();
			Users users=  Usersrepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("Could not found User"));
			recruiterProfile.setUserId(users); 	
			recruiterProfile.setUserAccountId(users.getUserId()); 
			
		}
		model.addAttribute("profile",recruiterProfile);
		String filename=""; 
		if(!multipartfile.getOriginalFilename().equals("")) {
			filename=org.springframework.util.StringUtils.cleanPath(Objects.requireNonNull(multipartfile.getOriginalFilename())); 
			recruiterProfile.setProfilePhoto(filename);
			
		}
		RecruiterProfile savedUser=recruiterProfileService.addNew(recruiterProfile);
		String UploadDir="photos/recruiter/"+savedUser.getUserAccountId();
		try {
			
			FileUploadUtil.saveFile(UploadDir, filename, multipartfile);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/dashboard/";
		
		
		
		
	}

}
