package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.entity.ConfirmToken;
import com.example.paydaytrade.enums.Exceptions;
import com.example.paydaytrade.exceptions.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;

    //@Value("${application.security.confirmation.url}")
    private final String confirmationPath = "http://localhost:8082/auth/confirm/";

    public void sendMail(String email, ConfirmToken confirmToken) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");


        try{
            System.out.println("-----------1-----------");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Account activation!");

            String confirmationUrl = confirmationPath + confirmToken.getJwt();
            String emailContent = "<html><body>Click the following link to confirm your account:"
                    + "<a href=\"" + confirmationUrl + "\">Activate Account!</a>"
                    + "</body></html>";

            mimeMessageHelper.setText(emailContent, true);
            System.out.println("-----------2-----------");
            javaMailSender.send(mimeMessage);
            System.out.println("-----------3-----------");

        }
        catch (MessagingException e) {
            System.out.println("-----------4-----------");
            throw new ApplicationException(Exceptions.MESSAGE_NOT_SEND_EXCEPTION);
        }

    }

}
