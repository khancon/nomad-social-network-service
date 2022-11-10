package com.akhan.nomadsocialnetworkservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.akhan.nomadsocialnetworkservice.validation.PasswordMatches;
import com.akhan.nomadsocialnetworkservice.validation.ValidPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class UserDto {
    @NotNull
    @NotEmpty
    private String firstName;
    
    @NotNull
    @NotEmpty
    private String lastName;
    
    @NotNull
    @NotEmpty
    private String email;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;
    
    // standard getters and setters
}
