package com.akhan.nomadsocialnetworkservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.akhan.nomadsocialnetworkservice.model.User;

public interface PostRepository extends MongoRepository<User, String> {
    
}
