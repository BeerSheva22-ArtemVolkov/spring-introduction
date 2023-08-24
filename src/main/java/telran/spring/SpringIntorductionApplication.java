package telran.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication // Указывает, что это bean
public class SpringIntorductionApplication { 

	public static void main(String[] args) {
		// run строит аппликационный контекст и помещает туда определенные bean-ы, управляемые объектом

		// 1арг - передается объект класса над которым стоят необходимые для построения аппликационного контекста аннотации (@SpringBootApplication)

		// Смысл - построить аппликационный контекст
		// Сканирует все package-ы, начниая с того, где находится этот класс (в данном случае - telran.spring)
		// Ищет аннотации:
		// - @RestController (с ним работает bin servlet)
		// - @ControllerAdvice
		// - @Service
		SpringApplication.run(SpringIntorductionApplication.class, args); 

		// с объектами работаем "мы", а с bean-ми Spring
		// bean - это объект управляемый Spring-ом, находящийся в аппликационном контексте

	}

}

// spring security - интеграция с протоколами

// bin-authentication обращается к bin-detail-service с данными юзера
// bin-authorization (принимает правила) ему передает bin-auth 
// bin-auth обращается к authorization-rules 