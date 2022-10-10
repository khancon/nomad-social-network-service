package com.akhan.nomadsocialnetworkservice.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.akhan.nomadsocialnetworkservice.validation.ValidEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    
    @Id
    private String id;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String phoneNumber;
    private String college;
    private String gradYear;
    private LocalDate dob;
    private String location;
    private String pronouns;
    List<String> eventInterests = new ArrayList<>();
    List<String> friends = new ArrayList<>();

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // public String getId(){
    //     return this.id;
    // }

    // public void setFirstName(String firstName){
    //     this.firstName = firstName;
    // }

    // public String getFirstName(){
    //     return this.firstName;
    // }

    // public void setLastName(String lastName){
    //     this.lastName = lastName;
    // }

    // public String getLastName(){
    //     return this.lastName;
    // }
}
