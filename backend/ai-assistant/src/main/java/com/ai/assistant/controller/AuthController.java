package com.ai.assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ai.assistant.entity.User;
import com.ai.assistant.repository.UserRepository;
import com.ai.assistant.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

 @Autowired
 AuthService service;
 @Autowired
private UserRepository UserRepository;

 @PostMapping("/register")
 public User register(@RequestBody User user){
  return service.register(user);
 }

 @PostMapping("/login")
 public User login(@RequestBody User user){
  return service.login(user.getEmail(), user.getPassword());
 }
 @PutMapping("/update")
public User updateUser(@RequestBody User user) {
    return UserRepository.save(user);
}

}
