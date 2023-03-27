package com.ed.productiontool;

import com.ed.productiontool.event.RefreshEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.swing.*;

@SpringBootApplication
@RequiredArgsConstructor
public class ProductionToolApplication {
    private final RefreshEvent refreshEvent;

    public static void main(String[] args) {
        SpringApplication.run(ProductionToolApplication.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner() {
        System.setProperty("java.awt.headless", "false");
        return args -> SwingUtilities.invokeLater(() -> new Processor(refreshEvent));
    }
}
