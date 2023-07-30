package telran.spring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Message;
import telran.spring.service.Sender;

// аппликационный контекст - место где находятся bean-ы
@RestController // означает, что на базе этого класса будет создан servlet
// этот метод будет вызываться, когда клиент отправляет GET-запрос на URL "sender"
// позволяет определить, какие методы контроллера обрабатывают определенные типы HTTP-запросов и URL-шаблоны
// TODO (с помощью нижней аннотации все запросы .../sender с любым типом будут обрабатываться здесь?)
@RequestMapping("sender")
// будет искать все bean-ы в аппликационном контексте, которые являются объектом класса, иплементирующего Sender
@RequiredArgsConstructor // конструктор для полей с final
@Slf4j
public class SenderController {

	final List<Sender> sendersList;
	Map<String, Sender> sendersMap = new HashMap<>(); // Sender - это bean, который иплементирует Sender
	final ObjectMapper mapper;

	@PostMapping
	// по типу message должен найти service
	// TODO
	ResponseEntity<String> send(@RequestBody @Valid Message message) {
		log.debug("controller received message {}", message);

		Sender sender = sendersMap.get(message.type);
		String resWrong = "Wrong message type " + message.type;
		String resRight = null;
		
		// Помещается ответ с кодом Bad Request
		ResponseEntity<String> res = ResponseEntity.badRequest().body(resWrong);

		if (sender != null) {
			try {
				resRight = sender.send(message);
				res = ResponseEntity.ok().body(resRight);
			} catch (Exception e) {
				res = ResponseEntity.badRequest().body(e.getMessage());
			}
		} else {
			log.error(resWrong);
		}
		return res;
	}

	@PostConstruct // бины уже построены (инф-я считывается из файла)
	void init() {
		log.info("registered senders: {}", sendersMap.keySet());
		sendersList.forEach(sender -> {
			sendersMap.put(sender.getType(), sender);
		});
		sendersList.forEach(sender -> mapper.registerSubtypes(sender.getMessageTypeObject()));
	}

	@PreDestroy // методы, аннотируемые этой аннтоацией будут вызваны (инф-я заносится в файл)
	void shutdown() {
		log.info("context close");
	}

}
