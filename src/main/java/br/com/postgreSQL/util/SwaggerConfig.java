package br.com.postgreSQL.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Projeto de extração de dados.")
                .description("Realiza a autenticação do usuario em um aplicativo (Authenticator)")
                .version("1.0")
                .contact(new Contact()
                    .name("luan.filipin")
                    .email("https://github.com/luan-filipin"))
            );
    }
}
