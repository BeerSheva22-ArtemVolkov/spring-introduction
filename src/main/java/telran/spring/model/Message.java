package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.*;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@Data // Конструкторы, геттеры, сеттеры, toString(), hashCode() и equals() создаются автоматически.
public class Message { 

	@NotEmpty
	@Pattern(regexp = "[a-z]{3,5}", message = "type value missmatches pattern") // message - котоырй будет выбрасываться
	public String type;
	
	@NotEmpty
	public String text;
}
