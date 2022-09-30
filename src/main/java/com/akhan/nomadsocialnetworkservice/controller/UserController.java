package com.akhan.nomadsocialnetworkservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.repository.UserRepository;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    
    @Autowired
    UserRepository userRepository;

    
}
