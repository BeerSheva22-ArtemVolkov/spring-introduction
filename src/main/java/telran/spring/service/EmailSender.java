package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.EmailMessage;
import telran.spring.model.Message;

//@Service("email") // naming convention
@Service // Указывает, что это bean
@Slf4j // логгер, который может быть конфигурируем спрингом
public class EmailSender implements Sender {

	static final String SERVICE_NAME = "email";
	
	@Override
	public String send(Message message) {
		log.debug("Email service received message {}", message); // message вставится вместо {}
		String res = errorMessage;
		if (message instanceof EmailMessage) {
			EmailMessage emailMessage = (EmailMessage) message;
			res = String.format("email sendedr text: %s has been sent to mail %s", emailMessage.text,
					emailMessage.emailAddress);
		} else {
			throw new IllegalArgumentException(res);
		}
		return res;
		// Бизнес-логика и методы для работы с сообщениями
	}

	@Override
	public String getType() {
		return SERVICE_NAME;
	}

	@Override
	public Class<? extends Message> getMessageType() {
		return EmailMessage.class;
	}

}
