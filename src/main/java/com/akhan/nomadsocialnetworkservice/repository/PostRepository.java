package com.akhan.nomadsocialnetworkservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.akhan.nomadsocialnetworkservice.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}
