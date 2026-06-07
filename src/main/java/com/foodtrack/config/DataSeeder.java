package com.foodtrack.config;

import com.foodtrack.entity.*;
import com.foodtrack.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor


/**
 * Kelas konfigurasi untuk DataSeeder di dalam spring boot.
 */
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PetaniRepository petaniRepository;
    private final KomoditasRepository komoditasRepository;
    private final StokPanganRepository stokPanganRepository;
    private final DistribusiRepository distribusiRepository;
    private final ChatbotLogRepository chatbotLogRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Hanya seed jika database kosong
        if (adminRepository.count() > 0) return;

        System.out.println("ðŸŒ± Seeding database...");

        // === Admin ===
        Admin admin = Admin.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .email("admin@foodtrack.id")
            .build();
        adminRepository.save(admin);

        // === Komoditas ===
        Komoditas beras = komoditasRepository.save(Komoditas.builder().namaKomoditas("Beras").satuan("Kg").build());
        Komoditas jagung = komoditasRepository.save(Komoditas.builder().namaKomoditas("Jagung").satuan("Kg").build());
        Komoditas kedelai = komoditasRepository.save(Komoditas.builder().namaKomoditas("Kedelai").satuan("Kg").build());
        Komoditas cabai = komoditasRepository.save(Komoditas.builder().namaKomoditas("Cabai").satuan("Kg").build());
        Komoditas bawang = komoditasRepository.save(Komoditas.builder().namaKomoditas("Bawang Merah").satuan("Kg").build());

        // === Petani ===
        Petani budi = petaniRepository.save(Petani.builder()
            .nama("Budi Santoso").kelompokTani("Tani Makmur").kontak("081234567890")
            .username("budi").password(passwordEncoder.encode("budi123")).build());

        Petani siti = petaniRepository.save(Petani.builder()
            .nama("Siti Aminah").kelompokTani("Suka Maju").kontak("081298765432")
            .username("siti").password(passwordEncoder.encode("siti123")).build());

        Petani agus = petaniRepository.save(Petani.builder()
            .nama("Agus Pratama").kelompokTani("Tani Makmur").kontak("085712345678")
            .username("agus").password(passwordEncoder.encode("agus123")).build());

        // === Stok Pangan ===
        StokPangan s1 = stokPanganRepository.save(StokPangan.builder()
            .petani(budi).komoditas(beras).jumlahMasuk(500).tanggalPanen(LocalDate.of(2024, 5, 15)).build());
        StokPangan s2 = stokPanganRepository.save(StokPangan.builder()
            .petani(budi).komoditas(jagung).jumlahMasuk(300).tanggalPanen(LocalDate.of(2024, 5, 20)).build());
        StokPangan s3 = stokPanganRepository.save(StokPangan.builder()
            .petani(siti).komoditas(beras).jumlahMasuk(750).tanggalPanen(LocalDate.of(2024, 6, 1)).build());
        StokPangan s4 = stokPanganRepository.save(StokPangan.builder()
            .petani(siti).komoditas(kedelai).jumlahMasuk(200).tanggalPanen(LocalDate.of(2024, 6, 5)).build());
        StokPangan s5 = stokPanganRepository.save(StokPangan.builder()
            .petani(agus).komoditas(cabai).jumlahMasuk(100).tanggalPanen(LocalDate.of(2024, 6, 10)).build());
        StokPangan s6 = stokPanganRepository.save(StokPangan.builder()
            .petani(agus).komoditas(bawang).jumlahMasuk(250).tanggalPanen(LocalDate.of(2024, 6, 12)).build());

        // === Distribusi ===
        distribusiRepository.save(Distribusi.builder()
            .stokPangan(s1).tujuan("Gudang Bulog Divre Jabar").statusPengiriman("Selesai").tanggalKirim(LocalDate.of(2024, 5, 25)).build());
        distribusiRepository.save(Distribusi.builder()
            .stokPangan(s2).tujuan("Pasar Induk Cipinang").statusPengiriman("Dikirim").tanggalKirim(LocalDate.of(2024, 5, 28)).build());
        distribusiRepository.save(Distribusi.builder()
            .stokPangan(s3).tujuan("Koperasi Tani Makmur").statusPengiriman("Menunggu").build());
        distribusiRepository.save(Distribusi.builder()
            .stokPangan(s4).tujuan("Pabrik Kecap Bango").statusPengiriman("Selesai").tanggalKirim(LocalDate.of(2024, 6, 8)).build());

        // === Chatbot Log ===
        chatbotLogRepository.save(ChatbotLog.builder()
            .petani(budi).userPrompt("Bagaimana cara mengatasi hama wereng?")
            .geminiResponse("Untuk mengatasi hama wereng, gunakan pestisida organik berbahan nimba dan atur pengairan sawah dengan teknik intermitten.")
            .timestamp(LocalDateTime.now().minusHours(2)).build());
        chatbotLogRepository.save(ChatbotLog.builder()
            .petani(siti).userPrompt("Berapa dosis pupuk KCL untuk padi?")
            .geminiResponse("Dosis pupuk KCL yang direkomendasikan untuk padi adalah 100-150 kg/ha. Aplikasikan pada umur 21 HST.")
            .timestamp(LocalDateTime.now().minusHours(1)).build());

        System.out.println("âœ… Database seeded successfully!");
        System.out.println("   Admin  â†’ username: admin   | password: admin123");
        System.out.println("   Petani â†’ username: budi    | password: budi123");
        System.out.println("   Petani â†’ username: siti    | password: siti123");
        System.out.println("   Petani â†’ username: agus    | password: agus123");
    }
}

