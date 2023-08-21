package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.annotation.JsonAlias;

import telran.spring.model.*;
import telran.spring.service.EmailSender;

// Строим аппл контекст как в нашей аппл
//@SpringBootTest(properties = {"app.security.user.password=pass", "app.security.admin.password=pass"})
@SpringBootTest(classes = { EmailSender.class })
class EmailSenderTest {

	@Autowired
	EmailSender sender;

	@Test
	void EmailSenderRightFlow() {

		EmailMessage message = new EmailMessage();
		message.type = "email";
		message.text = "text";
		message.emailAddress = "test@gmail.com";

		String expected = String.format("email sendedr text: %s has been sent to mail %s", message.text,
				message.emailAddress);

		assertEquals(expected, sender.send(message));
	}

	@Test
	// проверяем что бросает Exception если приходит не тот Message
	void EmailSenderWrongType() {

		TcpMessage message = new TcpMessage();
		message.type = "email";
		message.text = "text";
		message.setHostName("test@gmail.com");

		assertThrowsExactly(IllegalArgumentException.class, () -> sender.send(message));
	}
}

// 1. контроллер связанный с сервисом через интерфейс
// 2. функциональности с логами, валидациями и тестами
// 3. 
