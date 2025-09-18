package net.engineeringdigest.journalApp.controller;

import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    //Get all journal entries for a user
    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        List<JournalEntry> all = user.getJournalEntries();
        if(all == null || all.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(all);
    }

    // Create a new entry for a user
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(
            @RequestBody JournalEntry myEntry){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            journalEntryService.saveJournalEntry(myEntry,username);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Get a journal entry by ID
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(
            @PathVariable Long myId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        return user.getJournalEntries().stream()
                .filter(
                        entry -> entry.getId().equals(myId))
                                .findFirst()
                                .map(entry -> new ResponseEntity<>(entry,HttpStatus.OK))
                                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    //Delete a journal entry (only owner can delete)
    @DeleteMapping("id/{myId}")
    public ResponseEntity<Void> deleteJournalEntryById(
            @PathVariable Long myId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        journalEntryService.deleteJournalEntryById(myId,username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Update an existing entry
    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalById(
            @PathVariable Long id,
            @RequestBody JournalEntry newEntry ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        boolean entryExists = user.getJournalEntries()
                .stream()
                .anyMatch(entry -> entry.getId().equals(id));

        if(!entryExists){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry old = journalEntryService.findJournalEntryById(id);
        // Double-check ownership
        if (!old.getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            old.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            old.setContent(newEntry.getContent());
        }
        return ResponseEntity.ok(journalEntryService.saveJournalEntry(old,username));

    }
}
