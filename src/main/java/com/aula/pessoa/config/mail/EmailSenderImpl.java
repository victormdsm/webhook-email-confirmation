package com.aula.pessoa.config.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;



@Service
public class EmailSenderImpl {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine engine;

    public EmailSenderImpl(JavaMailSender javaMailSender, SpringTemplateEngine engine) {
        this.javaMailSender = javaMailSender;
        this.engine = engine;
    }

    @Value("${spring.mail.username}")
    private String sender;


    public void sendTemplateEmail(String nome, String linkConfirmacao, String email){
        try{

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("nome", nome);
            context.setVariable("linkConfirmacao", linkConfirmacao);

            String html = engine.process("confirm-email", context);

            helper.setFrom(sender);
            helper.setTo(email);
            helper.setSubject("Confirmação de criação de conta");
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }


    public String sendEmail(EmailDetails details){

        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception ex) {
            throw new RuntimeException("Falha ao enviar email", ex);
        }
    }


}
