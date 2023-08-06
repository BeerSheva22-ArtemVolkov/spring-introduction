package telran.spring.service;

import telran.spring.model.Message;	
public interface Sender {

	String errorMessage = "Message has a wrong type";
	
	String send(Message message);
	String getType();
	Class<? extends Message> getMessageType();
}
