package com.akhan.nomadsocialnetworkservice.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "events")
public class Event {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String location;

    @NotNull
    @NotEmpty
    private LocalDate date;

    private String description;
    private LocalTime time;
    private boolean isPublic;
    private boolean isPostable;
    private String userCreatorId;
    Map<String, String> tags = new HashMap<>();
    Set<String> userIdsOfAttending = new HashSet<>();
    Set<String> userIdsOfLiked = new HashSet<>();

    public Event(String name, String description, String location, LocalDate date, LocalTime time){
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public boolean addUserAttending(String userIdAttending){
        if(!this.userIdsOfAttending.contains(userIdAttending)){
            this.userIdsOfAttending.add(userIdAttending);
            return true;
        }
        return false;
    }

    public boolean addUserLiked(String userIdLiked){
        if(!this.userIdsOfLiked.contains(userIdLiked)){
            this.userIdsOfLiked.add(userIdLiked);
            return true;
        }
        return false;
    }
}
