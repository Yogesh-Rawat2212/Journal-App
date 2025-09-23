package net.engineeringdigest.journalApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false, unique = true) // Unique username, cannot be null
    private String username;

    private String email;
    private boolean sentimentAnalysis;

    @Column(nullable = false)
    private String password;

    // One user can have many journal entries
    //    Cascade = ALL → If you move/demolish the house, all the rooms go with it.
    //    OrphanRemoval = true → If you throw a room out of the house, it’s also destroyed, not left hanging around alone.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntry> journalEntries = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

}
