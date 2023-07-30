package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Message;
import telran.spring.model.TcpMessage;

//@Service("tcp") // naming convention
@Service // Указывает, что это bean
@Slf4j // логгер, который может быть конфигурируем спрингом
public class TcpSender implements Sender {

	static final String SERVICE_NAME = "tcp";
	
	@Override
	public String send(Message message) {
		log.debug("TCP service received message {}", message); // message вставится вместо {}
		String res = errorMessage;
		if (message instanceof TcpMessage) {
			TcpMessage tcpMessage = (TcpMessage) message;
			res = String.format("tcp sender text: %s has been sent to hostname %s, port %d", tcpMessage.text, tcpMessage.getHostName(), tcpMessage.getPort());
		} else {
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
		return TcpMessage.class;
	}

}
