package com.gopikrishna.jobportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gopikrishna.jobportal.entity.UsersType;
import com.gopikrishna.jobportal.repository.UsersTypeRepository;

@Service
public class UsersTypeService { 
	@Autowired
	private final UsersTypeRepository usersTypeRepository;
	
	public UsersTypeService(UsersTypeRepository usersTypeRepository) {
		this.usersTypeRepository=usersTypeRepository;
	}
	
	public List<UsersType> getAll(){
		return usersTypeRepository.findAll();
	}

}
