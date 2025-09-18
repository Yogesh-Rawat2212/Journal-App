package net.engineeringdigest.journalApp.controller;

import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> all = userService.getAllUsers();
        return ResponseEntity.ok(all);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userService.saveAdmin(user);
        return ResponseEntity.ok(savedUser);
    }

}
