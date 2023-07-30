package telran.spring.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data // getter, setter, toString, hash и тд (совокупность аннотаций)
public class TcpMessage extends Message {

	String hostName;
	@Min(value = 1024) // то же что и внизу
	@Max(5000) // то же что и наверху
	int port;
}

