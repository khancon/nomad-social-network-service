package com.akhan.nomadsocialnetworkservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.error.UserAlreadyExistException;
import com.akhan.nomadsocialnetworkservice.model.User;
import com.akhan.nomadsocialnetworkservice.model.UserDto;
import com.akhan.nomadsocialnetworkservice.service.IUserService;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RegistrationController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    //Registration
    public ResponseEntity<User> registerUserAccount(@Valid @RequestBody final UserDto user){
        try {
            final User registered = userService.registerNewUserAccount(user);
            LOGGER.debug("Registering user account with information: {}", user);
            return new ResponseEntity<User>(HttpStatus.OK);
        } catch (UserAlreadyExistException e) {
            LOGGER.error("Duplicate account already exists with given email address: {}", user.getEmail());
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Registration failed for input user with following error: {}", e.getLocalizedMessage());
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
