package pl.tcps.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDocumentation() {
        return new Docket (DocumentationType.SWAGGER_2).apiInfo(apiInformation());
    }

    private ApiInfo apiInformation(){
        return new ApiInfoBuilder()
                .title("The Cheapest Petrol Station - API Documentation")
                .description("TCPS is my Engineering work on Silesian University of Technology")
                .version("1.0.0")
                .contact(new Contact("Grzegorz Nowak", "https://github.com/Grzegorz863/PracaInzynierska.git", "grzenow582@student.polsl.pl"))
                .build();
    }
}
