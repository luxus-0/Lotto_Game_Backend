package pl.lotto.emailsender;

import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Log4j2
class EmailSenderImpl implements EmailSender {

    private static final String FROM_EMAIL = "lotto_generator@op.pl";
    private final JavaMailSender mailSender;

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailMessage sendEmail(EmailDetails emailDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDetails.to());
        message.setReplyTo(emailDetails.to());
        message.setSubject(emailDetails.subject());
        message.setText(emailDetails.text());
        mailSender.send(message);
        return new EmailMessage("Email send successfully");
    }

    public EmailMessage sendEmailWithAttachment(EmailDetails emailDetails) throws MessagingException {
        {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(emailDetails.to());
            mimeMessageHelper.setReplyTo(emailDetails.to());
            mimeMessageHelper.setSubject(emailDetails.subject());
            mimeMessageHelper.setText(emailDetails.text());

            mimeMessageHelper.addAttachment("lotto.png", new File("src/main/resources/Lotto_logo.png"));
            mailSender.send(mimeMessage);
            return new EmailMessage("Email send successfully");
        }
    }
}
