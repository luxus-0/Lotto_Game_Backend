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

@Service
@Log4j2
class EmailSenderImpl implements EmailSender {

    private final JavaMailSender mailSender;
    private static final String FROM_EMAIL = "lotto_generator@op.pl";

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public MailMessageDto sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setReplyTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            return new MailMessageDto("Email send successfully");
        } catch (Exception e) {
            return new MailMessageDto("Email not send!!!");
        }
    }

    public MailMessageDto sendEmailWithAttachment(String to, String subject, String text, String attachment) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(FROM_EMAIL);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setReplyTo(subject);
            mimeMessageHelper.setSubject(text);
            mimeMessageHelper.setText(attachment);

            FileSystemResource file = new FileSystemResource(new File(attachment));
            mimeMessageHelper.addAttachment(attachment, file);
            mailSender.send(mimeMessage);
            return new MailMessageDto("Email send successfully");
        } catch (MessagingException e) {
            return new MailMessageDto("Email not send");
        }
    }
}
