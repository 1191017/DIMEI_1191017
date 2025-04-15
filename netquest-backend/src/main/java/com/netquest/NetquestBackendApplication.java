package com.netquest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetquestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetquestBackendApplication.class, args);
    }
}
