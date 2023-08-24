package telran.spring.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RolesConfigurationImpl implements RolesConfiguration {

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		log.warn("SenderController - configure");
		httpSecurity.authorizeHttpRequests(
				requests -> requests.requestMatchers(HttpMethod.GET).authenticated().anyRequest().hasRole("ADMIN"));
	}

}
 