package com.gopikrishna.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gopikrishna.jobportal.entity.Users;
import com.gopikrishna.jobportal.repository.UsersRepository;
import com.gopikrishna.jobportal.util.CustomUserDetails;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UsersRepository usersRepository;
	@Autowired
	public CustomUserDetailsService(UsersRepository usersRepository) {
		this.usersRepository=usersRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Users user=  usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Could n't Found User"));
		return new CustomUserDetails(user);
	}

}
