package ort.proyecto.gestac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@Configuration
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ImportResource({"classpath*:applicationContext.xml"})
public class Application {

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
