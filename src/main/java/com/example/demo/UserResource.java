package com.example.demo;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    //1
    @GetMapping
    private List<UserDto> users() {
        return userController.readAll();
    }

    //2
    @GetMapping("/{id}")
    private UserDto user(@PathVariable Integer id) {
        return userController.getUserById(id);
    }

    //Mostramos el email a traves de introducir el id
    @GetMapping("/{id}/email}")
    private Map<String, String> email(@PathVariable Integer id) {
        return Collections.singletonMap("email", userController.getUserById(id).getEmail());
    }

    //3
    @PostMapping
    private UserDto newUser(@RequestBody UserDto userDto) {
        return userController.addUser(userDto);
    }

    //4
    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable Integer id) {
        userController.deleteUserById(id);
    }

    //5
    @PutMapping("/{id}")
    private UserDto replaceUser(@RequestBody UserDto userDto, @PathVariable Integer id){
        return userController.replaceUser(userDto,id);
    }

    //6
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody JsonPatch patch) {
        return userController.updateUser(id,patch);
    }
}