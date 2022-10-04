package com.akhan.nomadsocialnetworkservice.model;

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
    // List<String> eventIds = new ArrayList<>();
}
