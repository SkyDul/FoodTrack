package com.foodtrack.web;

import com.foodtrack.service.PetaniService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LandingController {
    private final PetaniService petaniService;

    @GetMapping("/")
    public String landingPage(Model model) {
        model.addAttribute("totalPetani", petaniService.count());
        return "landing/index";
    }

    @GetMapping("/login-petani")
    public String loginPetani() {
        return "petani/login";
    }
}
