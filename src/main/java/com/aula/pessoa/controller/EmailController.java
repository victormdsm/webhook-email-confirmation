package com.aula.pessoa.controller;

import com.aula.pessoa.config.mail.EmailDetails;
import com.aula.pessoa.config.mail.EmailSenderImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailController {

    private final EmailSenderImpl emailSender;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details)
    {
        String status
                = emailSender.sendEmail(details);
        return status;
    }
}
