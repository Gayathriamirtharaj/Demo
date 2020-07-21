package com.example.controller;


import com.example.service.UserService;
import com.example.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/v1/account")
public class UserController {

    @Autowired
    private UserService userService;
    private Integer id;
    private JsonPatch patchJson;

    @PostMapping("/users")
    public ResponseEntity create(@RequestBody User user) {
        userService.create(user);
        throw new UnsupportedOperationException();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity read(@PathVariable("id") Integer id) {
        userService.read(id);
        throw new UnsupportedOperationException();
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user,Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name=principal.getName();
        if(name!=user.getFirstName()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "you cannot access");}
        userService.update(id,user);
        throw new UnsupportedOperationException();

    }

   ObjectMapper objectMapper=new ObjectMapper();
    @PatchMapping(value = "/users/{id}",consumes = "application/json-patch+json")
    public  ResponseEntity<?> patch(@PathVariable("id") Integer id, @RequestBody JsonPatch patchJson) {
        this.id = id;
        this.patchJson = patchJson;
        try{
            User user= UserCollection.users.get(id);
            User userpatch=userService.patchUser(patchJson,user);
            UserCollection.users.set(id,userpatch);
            return ResponseEntity.ok(userpatch);


        }
        catch( JsonPatchException | JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.OK).build();
        }

    }



    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        throw new UnsupportedOperationException();
    }


    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserCollection> search(@RequestParam String username, Principal principal) {

        System.out.println("Search done by: " + principal.getName());
        System.out.println("Looking for: " + username);
        UserCollection userCollection = new UserCollection();
        User user = userService.search(username);
        Collections.singletonList(user).add((User) userCollection.users);
        return new ResponseEntity<>(userCollection, HttpStatus.OK);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody User user) {
        return userService.register(user);

    }


    }
