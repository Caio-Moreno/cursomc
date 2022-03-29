package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Cliente;
import com.caiomoreno.cursomc.domain.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(AbstractEmailService.class);

    @Value("${default.sender}")
    private String sender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Override
    public void sendOrderConfirmationEmail(Pedido obj) {
        sendEmail(prepareSimpleMailMessageFromPedido(obj));
    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
        SimpleMailMessage sm = new SimpleMailMessage();

        sm.setTo(obj.getCliente().getEmail());
        sm.setFrom(sender);
        sm.setSubject("Pedido confirmado. Código: " + obj.getId());
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText(obj.toString());
        return sm;
    }

    protected String htmlFromTemplatePedido(Pedido obj) {
        Context context = new Context();

        context.setVariable("pedido", obj);

        return templateEngine.process("email/confirmacaoPedido", context);
    }

    @Override
    public void sendOrderConfirmationHtmlEmail(Pedido obj) {
        try {
            log.info("AQUI1"+obj);
            sendHtmlEmail(prepareMimeMessageFromPedido(obj));
        } catch (MessagingException e) {
            sendOrderConfirmationEmail(obj);
        }
    }

    protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
        log.info("SENDER"+ sender);
        log.info("TO"+ obj.getCliente().getEmail());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setTo(obj.getCliente().getEmail());
        mmh.setFrom(sender);
        mmh.setSubject("Pedido confirmado! Código: " + obj.getId());
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(htmlFromTemplatePedido(obj), true);

        return mimeMessage;
    }

    @Override
    public void sendNewPasswordEmail(Cliente cliente, String newPass) {
        sendEmail(prepareNewPasswordEmail(cliente, newPass));
    }

    protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass){
        SimpleMailMessage sm = new SimpleMailMessage();

        sm.setTo(cliente.getEmail());
        sm.setFrom(sender);
        sm.setSubject("Sua senha foi alterada com sucesso!");
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText("Segue sua nova senha: "+newPass);
        return sm;
    }
}
