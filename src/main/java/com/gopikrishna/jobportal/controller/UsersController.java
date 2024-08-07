package com.gopikrishna.jobportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gopikrishna.jobportal.entity.Users;
import com.gopikrishna.jobportal.entity.UsersType;
import com.gopikrishna.jobportal.service.UsersService;
import com.gopikrishna.jobportal.service.UsersTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UsersController {
	@Autowired
	private final UsersTypeService usersTypeService;
	private final UsersService userService;
	public UsersController(UsersTypeService usersTypeService,UsersService userservice) {
		this.usersTypeService=usersTypeService;
		this.userService=userservice;
	}
	@GetMapping("/register")
	public String register(Model model) {
		List<UsersType>  userTypes=usersTypeService.getAll();
		model.addAttribute("getAllTypes", userTypes); 
		model.addAttribute("user",new Users());
		return "register";
	}
	@PostMapping("/register/new")
	public String Userregistration(@Valid Users users) {
//		System.out.println("User "+users);
		userService.addNew(users);
		return "redirect:/dashboard/";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/";
	}
	
}
