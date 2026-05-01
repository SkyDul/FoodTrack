package com.foodtrack.web;

import com.foodtrack.entity.Petani;
import com.foodtrack.service.PetaniService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PetaniService petaniService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api/test-register")
    public String testRegister() {
        Petani p = new Petani();
        p.setNama("Test User");
        p.setUsername("test1");
        p.setPassword("test");
        p.setKontak("12345");
        p.setKelompokTani("Kelompok 1");
        
        petaniService.save(p);
        
        Petani saved = petaniService.findByUsername("test1").get();
        return "Saved! Password hashed: " + saved.getPassword() + " Matches 'test'? " + passwordEncoder.matches("test", saved.getPassword());
    }
}
