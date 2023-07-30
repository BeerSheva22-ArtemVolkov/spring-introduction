package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Message;
import telran.spring.model.SmsMessage;

//@Service("sms") // naming convention
@Service // Указывает, что это bean
@Slf4j // логгер, который может быть конфигурируем спрингом
public class SmsSender implements Sender {

	static final String SERVICE_NAME = "sms";
	
	@Override
	public String send(Message message) {
		log.debug("SMS service received message {}", message); // message вставится вместо {}
		String res = "SMS sender haven't received SmsMessage";
		if (message instanceof SmsMessage) {
			SmsMessage smsMessage = (SmsMessage) message;
			res = String.format("sms sender text: %s has been sent to number %s", smsMessage.text, smsMessage.phoneNumber);
		} else {
			log.error("The message has wrong type");
			throw new IllegalArgumentException(res);
		}
		return res;
	}

	@Override
	public String getType() {
		return SERVICE_NAME;
	}
	
	@Override
	public Class<? extends Message> getMessageTypeObject() {
		return SmsMessage.class;
	}

}
