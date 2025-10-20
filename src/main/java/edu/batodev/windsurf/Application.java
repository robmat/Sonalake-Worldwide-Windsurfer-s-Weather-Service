package edu.batodev.windsurf;

import no.nav.security.mock.oauth2.MockOAuth2Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
        MockOAuth2Server server = new MockOAuth2Server();
        server.start(9000);
        String issuerId = "default";
        String wellKnownUrl = server.wellKnownUrl(issuerId).toString();
        logger.debug("wellKnownUrl: {}", wellKnownUrl);

		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
