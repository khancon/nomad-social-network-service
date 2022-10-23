package com.akhan.nomadsocialnetworkservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.model.ResponseObject;
import com.akhan.nomadsocialnetworkservice.model.User;
import com.akhan.nomadsocialnetworkservice.model.UserDto;
import com.akhan.nomadsocialnetworkservice.service.IUserService;
import com.akhan.nomadsocialnetworkservice.service.ResponseObjectService;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    @Autowired
    private ResponseObjectService rService;

    //Registration
    @PostMapping("/user/registration")
    public ResponseEntity<ResponseObject> registerUserAccount(@Valid @RequestBody final UserDto user){
        final User registered = userService.registerNewUserAccount(user);
        LOGGER.info("Registering user account with information: {}", registered.toString());
        return new ResponseEntity<ResponseObject>(rService.getResponseObject("200 OK", "success", registered), HttpStatus.OK);
        // try {
        //     final User registered = userService.registerNewUserAccount(user);
        //     LOGGER.debug("Registering user account with information: {}", user);
        //     return new ResponseEntity<ResponseObject>(rService.getResponseObject("200 OK", "success", registered), HttpStatus.OK);
        // } catch (UserAlreadyExistException e) {
        //     LOGGER.error("Duplicate account already exists with given email address: {}", user.getEmail());
        //     return new ResponseEntity<ResponseObject>(rService.getResponseObject("400 BAD REQUEST", "Duplicate account already exists", e.getLocalizedMessage()),HttpStatus.BAD_REQUEST);
        // } catch (Exception e) {
        //     LOGGER.error("Registration failed for input user with following error: {}", e.getLocalizedMessage());
        //     return new ResponseEntity<ResponseObject>(rService.getResponseObject("500 INTERNAL SERVER ERROR", "Registration failed", e),HttpStatus.INTERNAL_SERVER_ERROR);
        // }
    }

    @GetMapping("/user/registration/{email}")
    public ResponseEntity<ResponseObject> emailExists(@PathVariable("email") String email){
        Boolean emailExists = userService.emailExists(email);
        if(emailExists){
            final User user = userService.findUserByEmail(email);
            return new ResponseEntity<ResponseObject>(rService.getResponseObject("400 Bad Request", "User with email \'" + email + "\'' exists", user), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<ResponseObject>(rService.getResponseObject("200 OK", "User with email \'" + email + "\'' exists", null), HttpStatus.OK);
        }
    }


}
