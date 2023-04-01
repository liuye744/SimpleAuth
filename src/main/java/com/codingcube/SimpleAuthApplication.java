package com.codingcube;

import com.codingcube.aspect.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SimpleAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleAuthApplication.class, args);
    }
}
