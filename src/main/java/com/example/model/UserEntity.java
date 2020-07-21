package com.example.model;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity

@Table(name = "[users]")
@Data
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    private String mobile;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    List<Role> roles;

}
