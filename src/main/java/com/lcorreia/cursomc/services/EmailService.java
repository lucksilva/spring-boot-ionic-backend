package com.lcorreia.cursomc.services;

import com.lcorreia.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Pedido obj);

    void sendEmail(SimpleMailMessage msg);

    void sendHtmlEmail(MimeMessage msg);

    void sendOrderConfirmationHtmlEmail(Pedido obj);
}
