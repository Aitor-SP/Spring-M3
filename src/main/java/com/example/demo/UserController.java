package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Controller
public class UserController {

    UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    //1
    public List<UserDto> readAll() {
        return userDao.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    //2
    public UserDto getUserById(Integer id) {
        Optional<User> user = userDao.findById(id);
        return user.map(UserDto::new).orElse(null);
    }

    //3
    public UserDto addUser(UserDto userDto) {
        User user = new User(userDto);
        userDao.save(user);
        return userDto;
    }

    //4
    public void deleteUserById(Integer id) {
        userDao.deleteById(id);
    }

    //5
    public UserDto replaceUser(UserDto userDto, Integer id) {
        User user = new User(userDto);
        return userDao.findById(id).map(u ->{
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setFullName(user.getFullName());
            userDao.save(u);
            return new UserDto(u);
        }).orElseGet(() -> {
            return new UserDto(userDao.save(user));
        });
    }

    //6
    public ResponseEntity<UserDto> updateUser(Integer id, JsonPatch patch) {

        try {
            UserDto user = getUserById(id);
            UserDto userPatched = applyPatchToCustomer(patch, user);
            userDao.save(new User(userPatched));
            return ResponseEntity.ok(userPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();
    public UserDto applyPatchToCustomer(JsonPatch patch, UserDto targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, UserDto.class);
    }
}
