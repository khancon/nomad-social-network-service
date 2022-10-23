package com.akhan.nomadsocialnetworkservice.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/* (non-Javadoc)
 * @see com.akhan.nomadsocialnetworkservice.model.User#toString()
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "organizations")
public class OrganizationUser extends User {
    @NonNull
    @NotEmpty
    public String organizationName;
}
