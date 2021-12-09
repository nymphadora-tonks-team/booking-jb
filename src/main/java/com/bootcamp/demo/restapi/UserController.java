package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.User;
import com.bootcamp.demo.service.UserService;
import com.bootcamp.demo.service.exception.ItemNotFoundException;
import com.bootcamp.demo.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.function.Supplier;


@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> postUser(@RequestBody final User user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteUserById(@PathVariable(value = "id") final String id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> getUserByGeneric(final Supplier<User> userSupplier) {
        try {
            return new ResponseEntity<>(userSupplier.get(), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUserById/{id}")
    @ResponseBody
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") final String id) {
        return getUserByGeneric(() -> userService.getUserById(id));
    }

    @GetMapping("/getUserByEmail/{email}")
    @ResponseBody
    public ResponseEntity<Object> getUserByEmail(@PathVariable(value = "email") final String email) {
        return getUserByGeneric(() -> userService.getUserByEmail(email));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Set<User>> getUsers() {
        try {
            return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
