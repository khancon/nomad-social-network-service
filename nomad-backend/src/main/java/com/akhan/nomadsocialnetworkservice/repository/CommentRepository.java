package com.akhan.nomadsocialnetworkservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.akhan.nomadsocialnetworkservice.model.*;

public interface CommentRepository extends MongoRepository<Comment, String>{

}
