package telran.spring.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SmsMessage extends Message {

	@Pattern(regexp = "\\d{5,10}")
	public String phoneNumber;
}
