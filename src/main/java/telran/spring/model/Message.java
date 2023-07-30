package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@Data // Конструкторы, геттеры, сеттеры, toString(), hashCode() и equals() создаются автоматически.
public class Message {

	public String type;
	public String text;
}
