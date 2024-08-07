package com.gopikrishna.jobportal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gopikrishna.jobportal.entity.RecruiterProfile;
import com.gopikrishna.jobportal.entity.Users;
import com.gopikrishna.jobportal.repository.RecruiterProfileRepository;
import com.gopikrishna.jobportal.repository.UsersRepository;

@Service
public class RecruiterProfileService {
		private final RecruiterProfileRepository recruiterProfileRepository;
		private final UsersRepository usersRepository;
		@Autowired
		 public RecruiterProfileService(RecruiterProfileRepository recruiterRepository, UsersRepository usersRepository) {
	        this.recruiterProfileRepository = recruiterRepository;
	        this.usersRepository = usersRepository;
	    }
		
		public Optional<RecruiterProfile> getOne(Integer Id){
			return recruiterProfileRepository.findById(Id);
		}

		public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
			// TODO Auto-generated method stub
			return recruiterProfileRepository.save(recruiterProfile);
		}

		
		 public RecruiterProfile getCurrentRecruiterProfile() {
		        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		        if (!(authentication instanceof AnonymousAuthenticationToken)) {
		            String currentUsername = authentication.getName();
		            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		            Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
		            return recruiterProfile.orElse(null);
		        } else return null;
		 }
}
