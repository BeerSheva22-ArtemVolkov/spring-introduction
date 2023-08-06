package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.SenderController;
import telran.spring.model.Message;
import telran.spring.service.Sender;

@Service
class MockSender implements Sender {

	@Override
	public String send(Message message) {
		return "test";
	}

	@Override
	public String getType() {
		return "test";
	}

	@Override
	public Class<? extends Message> getMessageType() {
		return Message.class;
	}

}

@WebMvcTest({ SenderController.class, MockSender.class }) // говорит что бины веба будут загружены, но ни одного бина
															// апп контекста не будет загружено
// передается массив объектов клааса класс, которые предполагаеются бинами (должны входить в апп контекст)

// mvc Moc - веб сервер(нереальный) для теста с нереальными запросами
class SendersCotrollerTest {
	// в контроллере мы проверяем эндпоинты

	@Autowired // нужен для того чтобы выполнять методы имитирующие запросы
	MockMvc mockMvc; // будет передавать запросы вместо клиента

	@Autowired
	ObjectMapper mapper;

	Message message;
	String sendUrl = "http://localhost:8080/sender";
	String getTypesUrl = sendUrl;
	String isTypePathUrl = String.format("%s/type", sendUrl);

	@BeforeEach
	void setUp() {
		message = new Message();
		message.text = "test";
		message.type = "test";
	}

	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc); // mock готов к выполнению запроса, если этот тест проходит
	}

	@Test
	void sendRightFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("test", response);
	}

	@Test
	void notFoundFlow() throws Exception {
		message.type = "abc";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isNotFound()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(message.type + " type not found", response);
	}

	@Test
	void sendValidationViolationFlow() throws Exception {
		message.type = "123";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertTrue(response.contains("missmatches"));
	}

	@Test
	void getTypesTest() throws Exception {
		String responseJsonString = mockMvc.perform(get(getTypesUrl)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String[] typesResponse = mapper.readValue(responseJsonString, String[].class);
		assertArrayEquals(new String[] { "test" }, typesResponse);
	}

	@Test
	void isTypePathExists() throws Exception {
		// завивит от MockSender getType(), что описан выше
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean responseBoolean = mapper.readValue(responseJson, boolean.class);
		assertTrue(responseBoolean);
	}

	@Test
	void isTypePathNotExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test1")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean responseBoolean = mapper.readValue(responseJson, boolean.class);
		assertFalse(responseBoolean);
	}

	@Test
	void isTypePathParamExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean responseBoolean = mapper.readValue(responseJson, boolean.class);
		assertTrue(responseBoolean);
	}

	@Test
	void isTypePathParamNotExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test1")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean responseBoolean = mapper.readValue(responseJson, boolean.class);
		assertFalse(responseBoolean);
	}
	
	@Test
	void isTypePathParamMissing() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl)).andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals("isTypeExistsParam.type: must not be empty", responseJson);
	}
	
	private ResultActions getRequestBase(String messageJson) throws Exception {
		return mockMvc.perform(post(sendUrl).contentType(MediaType.APPLICATION_JSON).content(messageJson))
				.andDo(print());
	}
}
