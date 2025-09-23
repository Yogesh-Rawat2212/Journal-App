package net.engineeringdigest.journalApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to,String subject,String text ){
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(text);
            mail.setFrom("itachiizumi2212@gmail.com");
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Exception while sending mail : "+e);
        }
    }
}
