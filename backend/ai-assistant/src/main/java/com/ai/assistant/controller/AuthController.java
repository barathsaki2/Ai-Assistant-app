package com.ai.assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ai.assistant.entity.User;
import com.ai.assistant.repository.UserRepository;
import com.ai.assistant.service.AuthService;
import com.ai.assistant.config.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

 @Autowired
 AuthService service;

 @Autowired
 private UserRepository userRepository;

 @Autowired
 private JwtUtil jwtUtil;

 @PostMapping("/register")
 public User register(@RequestBody User user){
  return service.register(user);
 }

 @PostMapping("/login")
 public Map<String,Object> login(@RequestBody User user){

  User existing = service.login(user.getEmail(), user.getPassword());

  Map<String,Object> response = new HashMap<>();

  if(existing != null){

   String token = jwtUtil.generateToken(existing.getEmail());

   response.put("token", token);
   response.put("user", existing);

   return response;
  }

  response.put("error","Invalid credentials");
  return response;
 }

 @PutMapping("/update")
 public User updateUser(@RequestBody User user) {
    return userRepository.save(user);
 }
}