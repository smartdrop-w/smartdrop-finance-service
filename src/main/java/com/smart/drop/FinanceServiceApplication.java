package com.smart.drop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner printSwaggerUrl(Environment env) {
        return unused -> {
            String port = env.getProperty("server.port", "8085");
            String swaggerUrl = "http://localhost:" + port + "/swagger-ui/index.html";
            System.out.println("\n" +
                "╔════════════════════════════════════════════════════╗\n" +
                "║   💳 SmartDrop Finance & Planning Service Activo   ║\n" +
                "╠════════════════════════════════════════════════════╣\n" +
                "║  📖 Swagger UI: " + String.format("%-35s", swaggerUrl) + "║\n" +
                "║  🗄️  Puerto:     " + String.format("%-35s", port) + "║\n" +
                "╚════════════════════════════════════════════════════╝\n");
        };
    }
}
