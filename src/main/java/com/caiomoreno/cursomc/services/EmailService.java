package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Pedido obj);


    void sendEmail(SimpleMailMessage msg);
}
