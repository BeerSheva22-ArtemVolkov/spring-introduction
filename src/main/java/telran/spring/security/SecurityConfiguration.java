package telran.spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Configuration // Спринг будет вызывать методы, внутри них будет строиться бин
@Slf4j
public class SecurityConfiguration {

	// последующие значения берутся из некого конфиг файла
	// доступ берется из 'Run Congfiguration' -> 'Environment' -> там добавлять переменные
	@Value("${app.security.user.name:user}")
	String userNameForUser;
	
	@Value("${app.security.admin.name:admin}")
	String userNameForAdmin;
	
	@Value("${app.security.user.password:${USER_PASSWORD}}")
	String passwordForUser;
	
	@Value("${app.security.admin.password:${ADMIN_PASSWORD}}")
	String passwordForAdmin;	
	//
	
	
	@Bean // аннотация должна быть внутри класса с аннотацией @Configuration
	// будет зарегистрирован как бин и будет использоваться для конфигурации безопасности
	// Задаем свои Authorization rules (из схемы) вместо какой-то дефолтной из спринга
	// объект этого класса станет бином
	SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity
		// отключает механизм CSRF (Cross-Site Request Forgery), который защищает приложение от атак на подделку запросов
				.csrf(custom -> custom.disable())
		// отключает конфигурацию CORS (Cross-Origin Resource Sharing), что позволяет контролировать доступ к ресурсам с разных источников
				.cors(custom -> custom.disable())
		// определяет, какие HTTP запросы требуют авторизации
				.authorizeHttpRequests(
				// все запросы разрешены без авторизации
//						custom -> custom.anyRequest().authenticated())
				// GET могут выполнить все аутентифицированные, остальные - только с ролью ADMIN
						custom -> custom.requestMatchers(HttpMethod.GET).authenticated().anyRequest().hasAnyRole("ADMIN"))
				// при повторном запросе не будет проверяться пароль
						.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				// настраивает базовую HTTP аутентификацию
				// Customizer.withDefaults() - используется для настройки базовой аутентификации с настройками по умолчанию
				.httpBasic(Customizer.withDefaults())
				.build();
	} 

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	// Задаем свои bean-detail-service (из схемы) вместо какой-то дефолтной из спринга
	UserDetailsService getUserDetailsService() {
	  
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager() {
	     
			@Override
			public UserDetails loadUserByUsername(String username) {
				log.debug("User with username {} is trying access the application", username);
				return super.loadUserByUsername(username);
			}
		};
	    
	    manager.createUser(User.withUsername(userNameForUser).password(passwordForUser)
	        .passwordEncoder(s -> getPasswordEncoder().encode(s)).roles("USER").build());
	    manager.createUser(User.withUsername(userNameForAdmin).password(passwordForAdmin)
	        .passwordEncoder(s -> getPasswordEncoder().encode(s)).roles("USER", "ADMIN").build());
	    return manager;
	}
	
}
