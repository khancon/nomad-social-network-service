package com.akhan.nomadsocialnetworkservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.akhan.nomadsocialnetworkservice.model.*;

public interface TagRepository extends MongoRepository<Tag, String>{
    Tag findByLabel(String label);
}
