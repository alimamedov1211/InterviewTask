package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.request.BuyAndSellStockDto;
import com.example.paydaytrade.entity.ConfirmToken;
import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.enums.Exceptions;
import com.example.paydaytrade.exceptions.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Account activation!");

            String confirmationUrl = confirmationPath + confirmToken.getJwt();
            String emailContent = "<html><body>Click the following link to confirm your account:"
                    + "<a href=\"" + confirmationUrl + "\">Activate Account!</a>"
                    + "</body></html>";

            mimeMessageHelper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);

        }
        catch (MessagingException e) {
            throw new ApplicationException(Exceptions.MESSAGE_NOT_SEND_EXCEPTION);
        }

    }

    public void sendBuyMessage(User user, BuyAndSellStockDto request){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();

        simpleMailMessage.setTo(user.getMail());
        simpleMailMessage.setSubject("Notification about buying stock!");
        simpleMailMessage.setText("Hello " + user.getUsername() + ",\n\n" +
                "Your purchase with " + request.getStockId() + "stock id is succesfully completed." +
                "Thank you");

        javaMailSender.send(simpleMailMessage);
    }

    public void sendSellMessage(User user, BuyAndSellStockDto request){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();

        simpleMailMessage.setTo(user.getMail());
        simpleMailMessage.setSubject("Notification about selling stock!");
        simpleMailMessage.setText("Hello " + user.getUsername() + ",\n\n" +
                "Your purchase with " + request.getStockId() + "stock id is succesfully completed." +
                "Thank you");

        javaMailSender.send(simpleMailMessage);
    }

}
