package com.akhan.nomadsocialnetworkservice.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;

    private String content;
    private String userFirstName;
    private String userProfileImage;
    private Instant createdAt;
    private int likes;
}
