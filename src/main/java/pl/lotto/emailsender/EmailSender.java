package pl.lotto.emailsender;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static pl.lotto.emailsender.EmailMessageProvider.*;

@Service
class EmailSender {

    private final JavaMailSender mailSender;

    EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    void send() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(TO_EMAIL);
        message.setReplyTo(TO_EMAIL);
        message.setSubject(SUBJECT);
        message.setText(TEXT);
        mailSender.send(message);
    }
}
