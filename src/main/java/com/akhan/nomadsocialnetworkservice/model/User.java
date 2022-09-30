package com.akhan.nomadsocialnetworkservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User {
    
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private Date dob;
    private String phoneNumber;
    private String college;
    private String gradYear;
    private String email;
    private String password;
    private String location;
    private String pronouns;
    List<String> eventInterests = new ArrayList<>();
    List<String> friends = new ArrayList<>();
}
