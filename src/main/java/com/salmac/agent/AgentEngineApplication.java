package com.salmac.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.salmac.agent.engine.files.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class AgentEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentEngineApplication.class, args);
	}
}
