package com.engine;

import org.springframework.boot.SpringApplication;

public class TestNettingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.from(NettingEngineApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
