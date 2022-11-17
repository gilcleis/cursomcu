package com.gilclei.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.gilclei.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

//	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
