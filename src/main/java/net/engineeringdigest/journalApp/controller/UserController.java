package net.engineeringdigest.journalApp.controller;

import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.config.SpringSecurity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;

    // Update user by username
    @PutMapping
    public ResponseEntity<User>updateUser(@RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User userInDb  = userService.getUserByUsername(username);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return ResponseEntity.ok(userInDb);
    }

    // Delete user by id
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/weather")
    public ResponseEntity<?> greetings(@RequestParam String city){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        if(weatherResponse==null || weatherResponse.getCurrent()==null){
            return new ResponseEntity<>("Weather data not available for city: " + city, HttpStatus.BAD_REQUEST);
        }
        var response = Map.of(
                "user", auth.getName(),
                "city", city,
                "feelsLike", weatherResponse.getCurrent().getFeelslike(),
                "greeting", "Hi " + auth.getName() + ", Weather feels like " + weatherResponse.getCurrent().getFeelslike()
        );
        return ResponseEntity.ok(response);
    }


//    // Get user by id
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id){
//        User user = userService.getUserById(id);
//        return ResponseEntity.ok(user);
//    }


}
