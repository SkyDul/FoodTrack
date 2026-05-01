package com.foodtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodTrackApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("🌱 SiPangan is running at http://localhost:8080");
        System.out.println("🔑 Admin Login   : http://localhost:8080/admin/login");
        System.out.println("🌾 Petani Login   : http://localhost:8080/login-petani");
        System.out.println("==========================================================");
    }
}
