package com.foodtrack.web;

import com.foodtrack.entity.*;
import com.foodtrack.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/petani")
@RequiredArgsConstructor
public class PetaniWebController {

    private final PetaniService petaniService;
    private final KomoditasService komoditasService;
    private final StokPanganService stokPanganService;
    private final DistribusiService distribusiService;
    private final ChatbotService chatbotService;

    /** Helper: get logged-in Petani entity */
    private Petani getLoggedInPetani(Authentication auth) {
        return petaniService.findByUsername(auth.getName()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        
        model.addAttribute("petaniName", petani.getNama());
        model.addAttribute("kelompokTani", petani.getKelompokTani());
        model.addAttribute("totalStok", stokPanganService.findByPetaniId(petani.getIdPetani()).size());
        model.addAttribute("totalDistribusi", distribusiService.findByPetaniId(petani.getIdPetani()).size());
        model.addAttribute("totalChatbot", chatbotService.findByPetaniId(petani.getIdPetani()).size());
        model.addAttribute("recentStok", stokPanganService.findByPetaniId(petani.getIdPetani()));
        return "petani/dashboard";
    }

    // ====== STOK ======
    @GetMapping("/stok")
    public String listStok(Model model, Authentication auth) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        model.addAttribute("listStok", stokPanganService.findByPetaniId(petani.getIdPetani()));
        model.addAttribute("listKomoditas", komoditasService.findAll());
        return "petani/stok/list";
    }

    @GetMapping("/stok/tambah")
    public String catatStok(Model model) {
        model.addAttribute("stokPangan", new StokPangan());
        model.addAttribute("listKomoditas", komoditasService.findAll());
        return "petani/stok/form";
    }

    @PostMapping("/stok/simpan")
    public String simpanStok(@ModelAttribute("stokPangan") StokPangan stokPangan,
                             Authentication auth, RedirectAttributes ra) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        stokPangan.setPetani(petani);
        komoditasService.findById(stokPangan.getKomoditas().getIdKomoditas()).ifPresent(stokPangan::setKomoditas);
        stokPanganService.save(stokPangan);
        ra.addFlashAttribute("successMessage", "Stok panen berhasil dicatat!");
        return "redirect:/petani/stok";
    }

    @GetMapping("/stok/{id}")
    public String detailStok(@PathVariable Integer id, Model model, Authentication auth, RedirectAttributes ra) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        return stokPanganService.findById(id).map(s -> {
            // Security: only owner can view
            if (!s.getPetani().getIdPetani().equals(petani.getIdPetani())) {
                ra.addFlashAttribute("errorMessage", "Anda tidak memiliki akses ke data ini.");
                return "redirect:/petani/stok";
            }
            model.addAttribute("stok", s);
            return "petani/stok/detail";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMessage", "Stok tidak ditemukan.");
            return "redirect:/petani/stok";
        });
    }

    // ====== DISTRIBUSI ======
    @GetMapping("/distribusi")
    public String riwayatDistribusi(Model model, Authentication auth) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        var list = distribusiService.findByPetaniId(petani.getIdPetani());
        model.addAttribute("listDistribusi", list);
        model.addAttribute("totalDistribusi", list.size());
        model.addAttribute("totalSelesai", list.stream().filter(d -> "Selesai".equals(d.getStatusPengiriman())).count());
        model.addAttribute("totalProses", list.stream().filter(d -> !"Selesai".equals(d.getStatusPengiriman())).count());
        return "petani/distribusi/list";
    }

    // ====== CHATBOT ======
    @GetMapping("/chatbot")
    public String chatbot(Model model, Authentication auth) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        model.addAttribute("chatHistory", chatbotService.findByPetaniId(petani.getIdPetani()));
        return "petani/chatbot/index";
    }

    @PostMapping("/chatbot/kirim")
    public String kirimPertanyaan(@RequestParam("prompt") String prompt,
                                   Authentication auth, RedirectAttributes ra) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        chatbotService.askChatbot(petani, prompt);
        return "redirect:/petani/chatbot";
    }

    @PostMapping("/chatbot/hapus-semua")
    public String hapusSemuaChat(Authentication auth, RedirectAttributes ra) {
        Petani petani = getLoggedInPetani(auth);
        if (petani == null) return "redirect:/login-petani";
        chatbotService.deleteByPetaniId(petani.getIdPetani());
        ra.addFlashAttribute("successMessage", "Riwayat chat berhasil dihapus.");
        return "redirect:/petani/chatbot";
    }
}
