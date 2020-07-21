package com.example.vo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import com.example.model.Role;

import java.util.List;

@Data
@ToString
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String mobile;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    List<Role>roles;

}