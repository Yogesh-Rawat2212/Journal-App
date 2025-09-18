package net.engineeringdigest.journalApp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //when updating a user
    public void saveUser(User user){
        userRepository.save(user);
    }

    //when creating a new user
    @Transactional
    public User saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }
    @Transactional
    public User saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER","ADMIN"));
        return userRepository.save(user);
    }

    public List<User>getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found with id : "+id));
    }
    @Transactional
    public void deleteById(Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("Cannot delete. User not found with id : "+id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUsername(String username){
        userRepository.deleteByUsername(username);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found with username : "+username));
    }
}
//controller call -> service call -> repository