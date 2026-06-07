
package com.foodtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Kelas utama untuk menjalankan aplikasi Spring Boot FoodTrack.
 * Aplikasi ini mengelola ketahanan pangan, stok komoditas, dan distribusi.
 */
@SpringBootApplication
public class FoodTrackApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FoodTrackApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");

        System.out.println("==========================================================");
        System.out.println("🌱 FoodTrack is running at http://localhost:" + port);
        System.out.println("🔑 Admin Login   : http://localhost:" + port + "/admin/login");
        System.out.println("🌾 Petani Login   : http://localhost:" + port + "/login-petani");
        System.out.println("==========================================================");
    }
}
