package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.User;
import com.bootcamp.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping(path="/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User postUser(@RequestBody final User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteUserById(@PathVariable(value = "id") final String id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable(value = "id") final String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getUserByEmail/{email}")
    public User getUserByEmail(@PathVariable(value = "email") final String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Set<User>> getScooters() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}
