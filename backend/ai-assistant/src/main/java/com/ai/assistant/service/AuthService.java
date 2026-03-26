package com.ai.assistant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.assistant.entity.User;
import com.ai.assistant.repository.UserRepository;

@Service
public class AuthService {

 @Autowired
 UserRepository repo;

 public User register(User user){
  return repo.save(user);
 }

 public User login(String email,String password){
  return repo.findByEmail(email)
          .filter(u -> u.getPassword().equals(password))
          .orElse(null);
 }

}

