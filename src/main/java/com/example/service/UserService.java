package com.example.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Component;
import com.example.vo.LoginRequest;
import com.example.vo.LoginResponse;
import com.example.vo.RegistrationResponse;
import com.example.vo.User;
@Component

public interface UserService {

    LoginResponse login(LoginRequest loginRequest);

    RegistrationResponse register(User user);

    User search(String username);

    String create(User user);

    String delete(int id);

    User read(int id);

    User update(int id,User user);

    User patchUser(JsonPatch patchJson, User user) throws JsonPatchException, JsonProcessingException;


}