package ya.dev.crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${info.project.name}")
	private String projectName;
	
	@Value("${info.project.desc}")
	private String projectDescription;
	
	@Value("${info.project.version}")
	private String projectVersion;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("ya.dev.crm")).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(projectName).license("(C) M2i - CRM")
				.licenseUrl("https://www.m2iformation.fr/").description(projectDescription).version(projectVersion).build();	
	}
}
