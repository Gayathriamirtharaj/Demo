package com.example.service;

import com.example.exception.ServiceException;
import com.example.model.UserEntity;
import com.example.repository.UserRepository;
import com.example.security.JWTProvider;
import com.example.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JWTProvider jwtProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected RestTemplate restTemplate;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtProvider.createToken(username, userRepository.findByUsername(username).getRoles());
            return new LoginResponse(token);
        } catch (AuthenticationException e) {
            throw new ServiceException("Invalid username/password", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public RegistrationResponse register(User user) {

        if (!userRepository.existsByUsername(user.getUsername())) {

            // Save in Repository
            UserEntity userEntity = modelMapper.map(user, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity savedUserEntity = userRepository.save(userEntity);
            System.out.println("Saved User: " + savedUserEntity);


            // Create Token
            String token = jwtProvider.createToken(userEntity.getUsername(), userEntity.getRoles());


            return new RegistrationResponse(token);

        } else {
            throw new ServiceException("Username already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public User search(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new ServiceException("User Not Found", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(userEntity, User.class);
    }
     @Override
    public String create(User user)
     {
         UserEntity userEntity=modelMapper.map(user,UserEntity.class);
         UserEntity userexist=userRepository.findByUsername(user.getUsername());
         UserCollection userCollection = new UserCollection();
         if(userexist==null)
         {
             userRepository.save(userEntity);
             userCollection.users.add(user);
             return "added user";
         }
         else
         {
             throw new ResponseStatusException(HttpStatus.CONFLICT,"User already exists");
         }

     }

    @Override
    public String delete(int id) {
        UserEntity userEntity=modelMapper.map(id,UserEntity.class);
        if(!userRepository.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        UserCollection.users.remove(id);
        return "deleted";
    }

    @Override
    public User read(int id) {
        UserEntity userEntity = modelMapper.map(id, UserEntity.class);
        if (!userRepository.existsById(id)) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not exist");
        }
            return UserCollection.users.get(id);

    }
    @Override
    public User update(int id,User user){
        UserEntity userEntity = modelMapper.map(id, UserEntity.class);
        if(!userRepository.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User does not exist");
        }
        else
        {
            UserCollection.users.add(user);
            return UserCollection.users.get(id);
        }

    }
    ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public User patchUser(JsonPatch patchJson, User user) throws JsonPatchException, JsonProcessingException {
        JsonNode patched=patchJson.apply(objectMapper.convertValue(user, JsonNode.class));
        return objectMapper.treeToValue(patched,User.class);
    }




}
