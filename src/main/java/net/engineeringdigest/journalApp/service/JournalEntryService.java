package net.engineeringdigest.journalApp.service;

import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    @Transactional//kuch b fail hojata h to successful cheeje b rollback hongi
    public JournalEntry saveJournalEntry(JournalEntry journalEntry, String userName){
        User user = userService.getUserByUsername(userName);
        journalEntry.setDate(LocalDateTime.now());
        journalEntry.setUser(user);
        return journalEntryRepository.save(journalEntry);
    }

    public JournalEntry findJournalEntryById(Long id){
        return journalEntryRepository.findById(id).orElseThrow(()->new RuntimeException("Journal Entry not found with id : "+id));
    }

    @Transactional
    public void deleteJournalEntryById(Long id,String userName){
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Journal entry not found with id : "+id));
        if(!entry.getUser().getUsername().equals(userName)){
            throw new RuntimeException("You are not allowed to delete this entry!");
        }
        journalEntryRepository.deleteById(id);
    }

//    public List<JournalEntry> findByUsername(String username){
//
//    }

}
//controller call -> service call -> repository