package com.akhan.nomadsocialnetworkservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.akhan.nomadsocialnetworkservice.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
