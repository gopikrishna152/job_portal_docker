package com.gopikrishna.jobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gopikrishna.jobportal.entity.Users;
import java.util.List;


public interface UsersRepository extends JpaRepository<Users, Integer>{
		Optional<Users> findByEmail(String email);
}
