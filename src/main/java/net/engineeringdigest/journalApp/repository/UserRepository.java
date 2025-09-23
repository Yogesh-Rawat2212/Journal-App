package net.engineeringdigest.journalApp.repository;
import net.engineeringdigest.journalApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.email LIKE %:pattern% AND u.sentimentAnalysis = true")
    List<User> getUserForSentimentAnalysis(@Param("pattern") String pattern);

    //User ka email valid format me ho (regex se check).Aur sentimentAnalysis = true ho.
    //userRepository.getUserForSA("@gmail.com");




}
