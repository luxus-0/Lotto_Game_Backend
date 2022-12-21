package pl.lotto.emailsender;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmailReceiverFacade {

    private final EmailSender emailSender;

    public EmailReceiverFacade(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public ResponseEntity<String> sendEmailToUser(){
        emailSender.send();
        return ResponseEntity.ok("SENDING EMAIL...");
    }
}
