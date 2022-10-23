package com.akhan.nomadsocialnetworkservice.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.model.User;
import com.akhan.nomadsocialnetworkservice.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    UserRepository userRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @GetMapping("")
    public String home(){
        return "User space";
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id){
        Optional<User> _user = userRepository.findById(id);
        return _user.isPresent() ? new ResponseEntity<>(_user.get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        System.out.println("hit endpoint");
        try {
            System.out.println("attempting to save user...");
            User _user = userRepository.save(user);
            System.out.println("saved user");
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("UH OH");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/users/registration")
    // public ResponseEntity<User> registerUser(@RequestBody User user){

    // }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserFully(@PathVariable("id") String id, @RequestBody User userDetails){
        try {
            Optional<User> _user = userRepository.findById(id);
            if(_user.isPresent()){
                User user = _user.get();
                user.setFirstName(userDetails.getFirstName());
                user.setLastName(userDetails.getLastName());
                user.setPhoneNumber(userDetails.getPhoneNumber());
                user.setCollege(userDetails.getCollege());
                user.setGradYear(userDetails.getGradYear());
                user.setDob(userDetails.getDob());
                user.setEmail(userDetails.getEmail());
                user.setPassword(userDetails.getPassword());
                user.setLocation(userDetails.getLocation());
                user.setPronouns(userDetails.getPronouns());
                user.setEventCategoryInterests(userDetails.getEventCategoryInterests());
                user.setFriends(userDetails.getFriends());
                return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable("id") String id, @RequestBody Map<String, Object> changes){
        try {
            Optional<User> _user = userRepository.findById(id);
            if(_user.isPresent()){
                User user = _user.get();
                changes.forEach((change, value) -> {
                    // System.out.println(change + ": " + value);
                    LOGGER.info("Changing field {} for user {}", change, id);
                    switch(change){
                        case "firstName": user.setFirstName((String) value); break;
                        case "lastName": user.setLastName((String) value); break;
                        case "phoneNumber": user.setPhoneNumber((String) value); break;
                        case "college": user.setCollege((String) value); break;
                        case "gradYear": user.setGradYear((String) value); break;
                        case "dob": user.setDob(LocalDate.parse((String)value)); break;
                        case "email": user.setEmail((String) value); break;
                        case "password": user.setPassword((String) value); break;
                        case "location": user.setLocation((String) value); break;
                        case "pronouns": user.setPronouns((String) value); break;
                        // case "eventInterests": user.setEventInterests((List<String>) value); break;
                        // case "friends": user.setFriends((List<String>) value); break;
                    }
                });
                return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id){
        try{
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @DeleteMapping("/users")
    // public ResponseEntity<HttpStatus> deleteAllUsers(@PathVariable("id") String id){
    //     try{
    //         userRepository.deleteAll();
    //         return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}
