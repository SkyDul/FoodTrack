package com.foodtrack.web;

import com.foodtrack.entity.Petani;
import com.foodtrack.entity.StokPangan;
import com.foodtrack.service.DistribusiService;
import com.foodtrack.service.KomoditasService;
import com.foodtrack.service.PetaniService;
import com.foodtrack.service.StokPanganService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LandingController {
    private final PetaniService petaniService;
    private final StokPanganService stokPanganService;
    private final KomoditasService komoditasService;
    private final DistribusiService distribusiService;
    private final com.foodtrack.repository.AdminRepository adminRepository;

    @GetMapping("/")
    public String landingPage(Model model) {
        model.addAttribute("totalPetani", petaniService.count());
        long totalStok = stokPanganService.findAll().stream().mapToLong(StokPangan::getJumlahMasuk).sum();
        model.addAttribute("totalStok", totalStok);
        model.addAttribute("totalKomoditas", komoditasService.count());
        model.addAttribute("distribusiSelesai", distribusiService.countByStatus("Selesai"));
        return "landing/index";
    }

    @GetMapping("/login-petani")
    public String loginPetani() {
        return "petani/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login-hub";
    }

    @GetMapping("/register-petani")
    public String registerPetani(Model model) {
        model.addAttribute("petani", new Petani());
        return "petani/register";
    }

    @PostMapping("/register-petani")
    public String doRegisterPetani(@ModelAttribute("petani") Petani petani, RedirectAttributes ra) {
        try {
            if (petani.getUsername() != null) petani.setUsername(petani.getUsername().trim());
            if (petani.getPassword() != null) petani.setPassword(petani.getPassword().trim());
            
            // Check if username already exists in Petani or Admin
            if (petaniService.findByUsername(petani.getUsername()).isPresent() || adminRepository.findByUsername(petani.getUsername()).isPresent()) {
                ra.addFlashAttribute("errorMessage", "Username sudah digunakan.");
                return "redirect:/register-petani";
            }
            
            petaniService.save(petani);
            ra.addAttribute("successMsg", "Pendaftaran berhasil! Akun Anda kini aktif. Silakan login.");
            return "redirect:/login-petani";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Terjadi kesalahan: " + e.getMessage());
            return "redirect:/register-petani";
        }
    }
    @GetMapping("/lupa-password")
    public String lupaPasswordPage() {
        return "petani/forgot-password";
    }

    @PostMapping("/lupa-password")
    public String prosesLupaPassword(@org.springframework.web.bind.annotation.RequestParam("username") String username,
                                     @org.springframework.web.bind.annotation.RequestParam("kontak") String kontak,
                                     @org.springframework.web.bind.annotation.RequestParam("newPassword") String newPassword,
                                     RedirectAttributes ra) {
        try {
            java.util.Optional<Petani> optionalPetani = petaniService.findByUsername(username.trim());
            if (optionalPetani.isPresent()) {
                Petani petani = optionalPetani.get();
                if (petani.getKontak().equals(kontak.trim())) {
                    // Update password (assumes PetaniService encrypts it when saving, or Petani class does. Wait, PetaniService might not encrypt on save. Let's see PetaniService)
                    // Actually, looking at Register flow, PetaniService saves directly. Wait, does it encrypt? I will check PetaniService.
                    petani.setPassword(newPassword.trim());
                    petaniService.save(petani);
                    ra.addAttribute("successMsg", "Kata sandi berhasil direset! Silakan login dengan kata sandi baru.");
                    return "redirect:/login-petani";
                }
            }
            ra.addFlashAttribute("errorMessage", "Username atau Kontak tidak cocok dengan data kami.");
            return "redirect:/lupa-password";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Terjadi kesalahan: " + e.getMessage());
            return "redirect:/lupa-password";
        }
    }
}
