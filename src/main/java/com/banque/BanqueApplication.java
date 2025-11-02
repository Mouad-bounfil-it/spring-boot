package com.banque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BanqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanqueApplication.class, args);
        
        System.out.println("\n" +
                "=======================================================\n" +
                "   🏦 Application Banque Started Successfully! 🏦\n" +
                "=======================================================\n" +
                "   📱 Web Interface: http://localhost:8080/banque\n" +
                "   📚 API Documentation: http://localhost:8080/swagger-ui.html\n" +
                "   📖 OpenAPI Spec: http://localhost:8080/api-docs\n" +
                "=======================================================\n");
    }
}

