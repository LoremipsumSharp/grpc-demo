package io.loremipsum.grpc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication

public class GrpcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcDemoApplication.class, args);
	}

}
