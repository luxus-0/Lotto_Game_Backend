package pl.lotto.emailsender;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.lotto.emailsender.dto.MailMessageDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.File;

import static pl.lotto.emailsender.EmailMessageProvider.*;

@Service
@Log4j2
class EmailSenderImpl {

    private final JavaMailSender mailSender;

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public MailMessageDto sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(TO_EMAIL);
            message.setReplyTo(TO_EMAIL);
            message.setSubject(SUBJECT);
            message.setText(TEXT);
            mailSender.send(message);
            return new MailMessageDto("Email send successfully");
        }catch (Exception e){
            return new MailMessageDto("Email not send!!!");
        }
    }

    public MailMessageDto sendEmailWithAttachment(String attachment) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(FROM_EMAIL);
            mimeMessageHelper.setTo(TO_EMAIL);
            mimeMessageHelper.setReplyTo(TO_EMAIL);
            mimeMessageHelper.setSubject(SUBJECT);
            mimeMessageHelper.setText(TEXT);

            FileSystemResource file = new FileSystemResource(new File(attachment));
            mimeMessageHelper.addAttachment(attachment, file);
            mailSender.send(mimeMessage);
            return new MailMessageDto("Email send successfully");
        }catch (MessagingException e){
            return new MailMessageDto("Email not send");
        }
    }
}
