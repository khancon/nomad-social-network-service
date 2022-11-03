package com.akhan.nomadsocialnetworkservice.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Comparable<Comment>{
    @Id
    private String id;

    private String postId;
    private String content;
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userAge;
    private String userProfileImage;
    private Instant createdAt;
    Set<String> usersLiked = new HashSet<>();

    public boolean addLike(String userId){
        return this.usersLiked.add(userId);
    }

    public boolean removeLike(String userId){
        return this.usersLiked.remove(userId);
    }

    @Override
    public int compareTo(Comment o) {
        return o.getUsersLiked().size() > this.usersLiked.size() ? 1 : -1; //descending order (greatest to least)
    }

    
}
