package com.foodtrack.web;

import com.foodtrack.entity.*;
import com.foodtrack.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {

    private final PetaniService petaniService;
    private final KomoditasService komoditasService;
    private final StokPanganService stokPanganService;
    private final DistribusiService distribusiService;
    private final ChatbotService chatbotService;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;

    // ====== LOGIN ======
    @GetMapping("/login")
    public String login() { return "admin/login"; }

    // ====== DASHBOARD ======
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalPetani", petaniService.count());
        model.addAttribute("totalKomoditas", komoditasService.count());
        
        // Count today's stock only
        long todayStok = stokPanganService.findAll().stream()
            .filter(s -> s.getCreatedAt() != null && s.getCreatedAt().toLocalDate().equals(java.time.LocalDate.now()))
            .count();
        model.addAttribute("totalStok", todayStok);
        
        model.addAttribute("totalDistribusi", distribusiService.count());
        model.addAttribute("recentStok", stokPanganService.findRecent());
        model.addAttribute("recentChatbotLogs", chatbotService.findRecent());
        return "admin/dashboard";
    }

    // ====== PETANI CRUD ======
    @GetMapping("/petani")
    public String listPetani(Model model) {
        model.addAttribute("listPetani", petaniService.findAll());
        return "admin/petani/list";
    }

    @GetMapping("/petani/tambah")
    public String tambahPetani(Model model) {
        model.addAttribute("petani", new Petani());
        return "admin/petani/form";
    }

    @GetMapping("/petani/edit/{id}")
    public String editPetani(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return petaniService.findById(id).map(p -> {
            model.addAttribute("petani", p);
            return "admin/petani/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMessage", "Data petani tidak ditemukan.");
            return "redirect:/admin/petani";
        });
    }

    @PostMapping("/petani/simpan")
    public String simpanPetani(@Valid @ModelAttribute("petani") Petani petani,
                               BindingResult result, 
                               @RequestParam(value = "passwordRaw", required = false) String passwordRaw,
                               RedirectAttributes ra) {
        if (result.hasErrors()) return "admin/petani/form";

        boolean isNew = petani.getIdPetani() == null;
        
        if (isNew) {
            // New user: use provided password or default 'password123'
            if (passwordRaw == null || passwordRaw.isBlank()) {
                petani.setPassword("password123");
            } else {
                petani.setPassword(passwordRaw);
            }
        } else {
            // Edit user: if password field is empty, keep the old one from database
            if (passwordRaw == null || passwordRaw.isBlank()) {
                petaniService.findById(petani.getIdPetani()).ifPresent(old -> {
                    petani.setPassword(old.getPassword());
                });
            } else {
                petani.setPassword(passwordRaw);
            }
        }
        
        petaniService.save(petani);
        notificationService.createNotification(
            isNew ? "Petani baru '" + petani.getNama() + "' telah ditambahkan." : "Data petani '" + petani.getNama() + "' diperbarui.",
            "success", "group", "PETANI", null, "ROLE_ADMIN"
        );
        ra.addFlashAttribute("successMessage", isNew ? "Data petani berhasil ditambahkan!" : "Data petani berhasil diperbarui!");
        return "redirect:/admin/petani";
    }

    @PostMapping("/petani/hapus/{id}")
    public String hapusPetani(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            petaniService.deleteById(id);
            ra.addFlashAttribute("successMessage", "Data petani berhasil dihapus.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("errorMessage", "Gagal menghapus! Petani ini masih memiliki data stok pangan atau riwayat chatbot yang terhubung. Harap hapus data terkait terlebih dahulu.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Terjadi kesalahan saat menghapus data petani.");
        }
        return "redirect:/admin/petani";
    }

    // ====== KOMODITAS CRUD ======
    @GetMapping("/komoditas")
    public String listKomoditas(Model model) {
        model.addAttribute("listKomoditas", komoditasService.findAll());
        return "admin/komoditas/list";
    }

    @GetMapping("/komoditas/tambah")
    public String tambahKomoditas(Model model) {
        model.addAttribute("komoditas", new Komoditas());
        return "admin/komoditas/form";
    }

    @GetMapping("/komoditas/edit/{id}")
    public String editKomoditas(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return komoditasService.findById(id).map(k -> {
            model.addAttribute("komoditas", k);
            return "admin/komoditas/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMessage", "Komoditas tidak ditemukan.");
            return "redirect:/admin/komoditas";
        });
    }

    @PostMapping("/komoditas/simpan")
    public String simpanKomoditas(@Valid @ModelAttribute("komoditas") Komoditas komoditas,
                                   BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "admin/komoditas/form";
        boolean isNew = komoditas.getIdKomoditas() == null;
        komoditasService.save(komoditas);
        notificationService.createNotification(
            isNew ? "Komoditas baru '" + komoditas.getNamaKomoditas() + "' telah ditambahkan." : "Komoditas '" + komoditas.getNamaKomoditas() + "' diperbarui.",
            "success", "agriculture", "KOMODITAS", null, "ROLE_ADMIN"
        );
        ra.addFlashAttribute("successMessage", isNew ? "Komoditas berhasil ditambahkan!" : "Komoditas berhasil diperbarui!");
        return "redirect:/admin/komoditas";
    }

    @PostMapping("/komoditas/hapus/{id}")
    public String hapusKomoditas(@PathVariable Integer id, RedirectAttributes ra) {
        komoditasService.deleteById(id);
        ra.addFlashAttribute("successMessage", "Komoditas berhasil dihapus.");
        return "redirect:/admin/komoditas";
    }

    // ====== STOK PANGAN CRUD ======
    @GetMapping("/stok")
    public String listStok(Model model) {
        model.addAttribute("listStok", stokPanganService.findAll());
        model.addAttribute("listKomoditas", komoditasService.findAll());
        model.addAttribute("listPetani", petaniService.findAll());
        return "admin/stok/list";
    }

    @GetMapping("/stok/tambah")
    public String tambahStok(Model model) {
        model.addAttribute("stokPangan", new StokPangan());
        model.addAttribute("listPetani", petaniService.findAll());
        model.addAttribute("listKomoditas", komoditasService.findAll());
        return "admin/stok/form";
    }

    @GetMapping("/stok/edit/{id}")
    public String editStok(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return stokPanganService.findById(id).map(s -> {
            model.addAttribute("stokPangan", s);
            model.addAttribute("listPetani", petaniService.findAll());
            model.addAttribute("listKomoditas", komoditasService.findAll());
            return "admin/stok/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMessage", "Data stok tidak ditemukan.");
            return "redirect:/admin/stok";
        });
    }

    @PostMapping("/stok/simpan")
    public String simpanStok(@ModelAttribute("stokPangan") StokPangan stokPangan, RedirectAttributes ra) {
        boolean isNew = stokPangan.getIdStok() == null;
        // Load related entities
        petaniService.findById(stokPangan.getPetani().getIdPetani()).ifPresent(stokPangan::setPetani);
        komoditasService.findById(stokPangan.getKomoditas().getIdKomoditas()).ifPresent(stokPangan::setKomoditas);
        stokPanganService.save(stokPangan);
        notificationService.createNotification(
            "Stok baru: " + stokPangan.getJumlahMasuk() + " " + stokPangan.getKomoditas().getSatuan() + " " + stokPangan.getKomoditas().getNamaKomoditas() + " dari " + stokPangan.getPetani().getNama(),
            "info", "inventory_2", "STOK",
            stokPangan.getPetani().getIdPetani(), "ROLE_PETANI"
        );
        ra.addFlashAttribute("successMessage", isNew ? "Stok pangan berhasil ditambahkan!" : "Stok pangan berhasil diperbarui!");
        return "redirect:/admin/stok";
    }

    @PostMapping("/stok/hapus/{id}")
    public String hapusStok(@PathVariable Integer id, RedirectAttributes ra) {
        stokPanganService.deleteById(id);
        ra.addFlashAttribute("successMessage", "Data stok pangan berhasil dihapus.");
        return "redirect:/admin/stok";
    }

    // ====== DISTRIBUSI CRUD ======
    @GetMapping("/distribusi")
    public String listDistribusi(Model model) {
        model.addAttribute("listDistribusi", distribusiService.findAll());
        return "admin/distribusi/list";
    }

    @GetMapping("/distribusi/tambah")
    public String tambahDistribusi(Model model) {
        model.addAttribute("distribusi", new Distribusi());
        model.addAttribute("listStok", stokPanganService.findAvailable());
        model.addAttribute("listPetani", petaniService.findAll());
        return "admin/distribusi/form";
    }

    @PostMapping("/distribusi/simpan")
    public String simpanDistribusi(@ModelAttribute("distribusi") Distribusi distribusi, RedirectAttributes ra) {
        boolean isNew = distribusi.getIdDistribusi() == null;
        stokPanganService.findById(distribusi.getStokPangan().getIdStok()).ifPresent(distribusi::setStokPangan);
        if (distribusi.getStatusPengiriman() == null) distribusi.setStatusPengiriman("Menunggu");
        distribusiService.save(distribusi);
        notificationService.createNotification(
            "Distribusi " + distribusi.getStokPangan().getKomoditas().getNamaKomoditas() + " ke " + distribusi.getTujuan() + " telah dicatat.",
            "info", "local_shipping", "DISTRIBUSI",
            distribusi.getStokPangan().getPetani().getIdPetani(), "ROLE_PETANI"
        );
        ra.addFlashAttribute("successMessage", isNew ? "Distribusi berhasil ditambahkan!" : "Distribusi berhasil diperbarui!");
        return "redirect:/admin/distribusi";
    }

    @PostMapping("/distribusi/status/{id}")
    public String updateStatusDistribusi(@PathVariable Integer id, RedirectAttributes ra) {
        distribusiService.advanceStatus(id);
        ra.addFlashAttribute("successMessage", "Status distribusi berhasil diperbarui.");
        return "redirect:/admin/distribusi";
    }

    @PostMapping("/distribusi/hapus/{id}")
    public String hapusDistribusi(@PathVariable Integer id, RedirectAttributes ra) {
        distribusiService.deleteById(id);
        ra.addFlashAttribute("successMessage", "Distribusi berhasil dihapus.");
        return "redirect:/admin/distribusi";
    }

    // ====== CHATBOT LOG ======
    @GetMapping("/chatbot")
    public String listChatbot(Model model) {
        model.addAttribute("listPetani", petaniService.findAll());
        model.addAttribute("listChatbotLog", chatbotService.findAll());
        return "admin/chatbot/log";
    }

    @PostMapping("/chatbot/hapus/{id}")
    public String hapusChatbot(@PathVariable Integer id, RedirectAttributes ra) {
        chatbotService.deleteById(id);
        ra.addFlashAttribute("successMessage", "Log chatbot berhasil dihapus.");
        return "redirect:/admin/chatbot";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return "admin/settings";
    }

    @GetMapping("/laporan")
    public String globalReport(Model model) {
        System.out.println(">>> DEBUG: Entering globalReport mapping...");
        model.addAttribute("active", "laporan");
        
        System.out.println(">>> DEBUG: Counting Petani...");
        model.addAttribute("totalPetani", petaniService.count());
        
        System.out.println(">>> DEBUG: Counting Komoditas...");
        model.addAttribute("totalKomoditas", komoditasService.count());
        
        System.out.println(">>> DEBUG: Counting Stok...");
        model.addAttribute("totalStok", stokPanganService.count());
        
        System.out.println(">>> DEBUG: Counting Distribusi...");
        model.addAttribute("totalDistribusi", distribusiService.count());
        
        System.out.println(">>> DEBUG: Fetching all Stok...");
        model.addAttribute("listStok", stokPanganService.findAll());
        
        System.out.println(">>> DEBUG: Fetching all Distribusi...");
        model.addAttribute("listDistribusi", distribusiService.findAll());
        
        System.out.println(">>> DEBUG: Rendering admin/laporan/global...");
        return "admin/laporan/global";
    }
}
