package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<UserDto> readAll() {
        List<UserDto> users = userDao.findAll().stream().map(UserDto::new).collect(Collectors.toList());
        return users;
    }

    public UserDto getUserById(Integer id) {
        Optional<User> user = userDao.findById(id);
        return user.map(UserDto::new).orElse(null);
    }

    public UserDto addUser(UserDto userDto) {
        User user = new User(userDto);
        userDao.save(user);
        return userDto;
    }

    public void deleteUserById(Integer id) {
        userDao.deleteById(id);
    }

    public UserDto updateUser(UserDto userDto, Integer id) {
        User user = new User(userDto);
        return userDao.findById(id).map(u ->{
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setFullName(user.getFullName());
            userDao.save(u);
            return new UserDto(u);
        }).orElseGet(() -> {
           UserDto userDto1 = new UserDto(userDao.save(user));
           return userDto1;
        });
    }
}
