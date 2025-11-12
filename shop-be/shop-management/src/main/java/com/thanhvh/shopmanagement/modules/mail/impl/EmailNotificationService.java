package com.thanhvh.shopmanagement.modules.mail.impl;

import com.thanhvh.shopmanagement.modules.mail.IEmailNotificationService;
import com.thanhvh.shopmanagement.modules.mail.model.Attachment;
import com.thanhvh.shopmanagement.modules.mail.model.SendMailModel;
import com.thanhvh.shopmanagement.util.FileUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class EmailNotificationService implements IEmailNotificationService {
    public static final String MAIL_SENDER = "sender";
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public EmailNotificationService(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    public void handle(SendMailModel request) {
        for (String toEmail : request.getTargets()) {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setSubject(request.getSubject());
                mimeMessageHelper.setFrom(mailProperties.getProperties().get(MAIL_SENDER));
                mimeMessageHelper.setTo(toEmail);
                mimeMessageHelper.setText(request.getContent(), request.isHtmlContent());
                addCcEmails(mimeMessageHelper, request.getCcEmails());
                addBccEmails(mimeMessageHelper, request.getBccEmails());
                addAttachments(mimeMessageHelper, request.getAttachments());
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                log.warn("Error Send Email to: {} Error: {}", toEmail, e.getMessage());
            }
        }
    }

    private void addCcEmails(MimeMessageHelper mimeMessageHelper, Set<String> ccEmails) throws MessagingException {
        if (ccEmails != null) {
            for (String cc : ccEmails) {
                mimeMessageHelper.addCc(cc);
            }
        }
    }

    private void addBccEmails(MimeMessageHelper mimeMessageHelper, Set<String> bccEmails) throws MessagingException {
        if (bccEmails != null) {
            for (String bcc : bccEmails) {
                mimeMessageHelper.addBcc(bcc);
            }
        }
    }

    private void addAttachments(MimeMessageHelper mimeMessageHelper, List<Attachment> attachments) throws MessagingException {
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                ByteArrayDataSource dataSource = new ByteArrayDataSource(
                        FileUtil.getByteFromFilePath(attachment.getFilePath()),
                        FileUtil.getMimeTypeFromFilePath(attachment.getFilePath())
                );
                mimeMessageHelper.addAttachment(attachment.getFileName(), dataSource);
            }
        }
    }
}
