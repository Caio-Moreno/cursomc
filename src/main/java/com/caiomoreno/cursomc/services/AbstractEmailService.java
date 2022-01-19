package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService{


    @Value("${default.sender}")
    private String sender;

    @Override
    public void sendOrderConfirmationEmail(Pedido obj){
        sendEmail(prepareSimpleMailMessageFromPedido(obj));
    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj){
        SimpleMailMessage sm = new SimpleMailMessage();

        sm.setTo(obj.getCliente().getEmail());
        sm.setFrom(sender);
        sm.setSubject("Pedido confirmado. Código: "+obj.getId());
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText(obj.toString());
        return sm;
    }
}
