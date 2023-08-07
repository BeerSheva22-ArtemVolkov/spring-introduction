package telran.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Спринг будет вызывать методы, внутри них будет строиться бин
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean // Authorization rules из нашей схемы
	SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity.csrf(custom -> custom.disable())
				.cors(custom -> custom.disable())
				.authorizeHttpRequests(
						custom -> custom.anyRequest().permitAll())
				.build();
	} 

}
