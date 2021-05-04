package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController //Vista para una maquina
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/users";
    UserController userController;

    @Autowired
    public UserResource(UserController userController) {
        this.userController = userController;
    }

    @GetMapping
    private List<UserDto> users() {
        return userController.readAll();
    }

    @GetMapping("/{id}")
    private UserDto user(@PathVariable Integer id) {
        return userController.getUserById(id);
    }

    @GetMapping("/{id}/email}")
    private Map<String, String> email(@PathVariable Integer id) {
        return Collections.singletonMap("email", userController.getUserById(id).getEmail());
    }

    @PostMapping
    private UserDto newUser(@RequestBody UserDto userDto) {
        return userController.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable Integer id) {
        userController.deleteUserById(id);
    }

    @PutMapping("/{id}")
    private UserDto replaceUser(@RequestBody UserDto userDto, @PathVariable Integer id){
        return userController.updateUser(userDto, id);
    }
}