package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void TestSendMail(){
        emailService.sendEmail(
                "www.yogeshrawat99@gmail.com",
                "Testing my mail in spring boot",
                "Arey bhai dekhke chalao"

        );
    }

}
