package com.akhan.nomadsocialnetworkservice.model;

import java.util.HashSet;
import java.util.Set;
// import java.util.ArrayList;
// import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tags")
public class Tag {
    @Id
    private String id;

    private String label;
    private String labelDescription;
    Set<String> eventIds = new HashSet<>();

    public Tag(String label){
        this.label = label;
    }

    public boolean addEventId(String eventId){
        if(!this.eventIds.contains(eventId)){
            this.eventIds.add(eventId);
            return true;
        }
        return false;
    }
}
