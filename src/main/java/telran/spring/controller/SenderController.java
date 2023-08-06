package telran.spring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.exception.NotFoundException;
import telran.spring.model.Message;
import telran.spring.service.Sender;

// аппликационный контекст - место где находятся bean-ы
@RestController // означает, что на базе этого класса будет создан servlet
// этот метод будет вызываться, когда клиент отправляет GET-запрос на URL "sender"
// позволяет определить, какие методы контроллера обрабатывают определенные типы HTTP-запросов и URL-шаблоны
@RequestMapping("sender")
// будет искать все bean-ы в аппликационном контексте, которые являются объектом класса, иплементирующего Sender
@RequiredArgsConstructor // конструктор для полей с final
@Slf4j
@Validated
public class SenderController {

	final List<Sender> sendersList; // spring соберет автоматически за счет @RequiredArgsConstructor
	Map<String, Sender> sendersMap = new HashMap<>(); // Sender - это bean, который иплементирует Sender
	final ObjectMapper mapper;

	@PostMapping
	// по типу message должен найти service
	String send(@RequestBody @Valid Message message) {
		log.debug("controller received message {}", message);

		Sender sender = sendersMap.get(message.type);
		String res = "Email sender have not received EmailMessage";
		String resWrong = message.type + " type not found";

		// Помещается ответ с кодом Bad Request
//		ResponseEntity<String> res = ResponseEntity.badRequest().body(resWrong);

		if (sender != null) {
			res = sender.send(message);
		} else {
			throw new NotFoundException(resWrong);
		}
		return res;
	}

	@GetMapping
	Set<String> getTypes() {
		return sendersMap.keySet();
	}

	@GetMapping("type/{typeName}")
	boolean isTypeExistsPath(@PathVariable(name = "typeName") String type) {
		log.debug("Type inside a path {}", type);
		return sendersMap.containsKey(type);
	}

	// с параметром
	@GetMapping("type")
	boolean isTypeExistsParam(@RequestParam(name = "type", defaultValue = "") @NotEmpty String type) {
		log.debug("Type inside a parameter {}", type);
		return sendersMap.containsKey(type);
	}

	@PostConstruct // бины уже построены (инф-я считывается из файла)
	void init() {
		sendersList.forEach(sender -> {
			sendersMap.put(sender.getType(), sender);
		});
		sendersList.forEach(sender -> mapper.registerSubtypes(sender.getMessageType()));
		log.info("registered senders: {}", sendersMap.keySet());
	}

	@PreDestroy // методы, аннотируемые этой аннтоацией будут вызваны (инф-я заносится в файл)
	void shutdown() {
		log.info("context close");
	}

}
