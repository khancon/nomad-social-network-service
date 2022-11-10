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
    List<String> comments = new ArrayList<>();
    // Queue<Comment> top10Comments = new PriorityQueue<>();

    public boolean addComment(String commentId){
        return comments.add(commentId);
    }

    public boolean removeComment(String commentId){
        return comments.remove(commentId);
    }

}
