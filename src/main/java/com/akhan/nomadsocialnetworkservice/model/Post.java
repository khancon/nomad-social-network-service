package com.akhan.nomadsocialnetworkservice.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    private String eventId;
    private Instant createdAt;
    private String userCreatorId;
    List<User> usersLiked = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();
}
