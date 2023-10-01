package com.codingcube.simpleauth;

import com.codingcube.simpleauth.annotation.SimpleCache;
import com.codingcube.simpleauth.limit.annotation.EnableDynamicLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@SimpleCache
public class SimpleAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleAuthApplication.class, args);
    }
}
