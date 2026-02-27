package com.cyberlearn.plataforma; 

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ServerLinkLogger {

    @EventListener(ApplicationReadyEvent.class)
    public void logServerLink() {
        System.out.println("\n----------------------------------------------------------");
        System.out.println("  Aplicação CyberLearn rodando em: http://localhost:8080/index.html");
        System.out.println("----------------------------------------------------------\n");
    }
}