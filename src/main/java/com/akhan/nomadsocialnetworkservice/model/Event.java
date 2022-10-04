package com.akhan.nomadsocialnetworkservice.model;

import java.util.Date;
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
@Document(collection = "events")
public class Event {
    @Id
    private String id;

    private String name;
    private String description;
    private String location;
    private Date dateTime;
    private User creator;
    List<Tag> tags = new ArrayList<>();

    public Event(String name, String description, String location, Date dateTime){
        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
    }
}
