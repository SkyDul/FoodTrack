package com.foodtrack.service;

import com.foodtrack.entity.ChatbotLog;
import com.foodtrack.entity.Petani;
import com.foodtrack.repository.ChatbotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotService {
    private final ChatbotLogRepository chatbotLogRepository;

    public List<ChatbotLog> findAll() { return chatbotLogRepository.findAll(); }
    public List<ChatbotLog> findByPetaniId(Integer id) { return chatbotLogRepository.findByPetaniIdPetaniOrderByTimestampDesc(id); }
    public List<ChatbotLog> findRecent() { return chatbotLogRepository.findTop5ByOrderByTimestampDesc(); }
    public void deleteById(Integer id) { chatbotLogRepository.deleteById(id); }

    @Transactional
    public void deleteByPetaniId(Integer id) { chatbotLogRepository.deleteByPetaniIdPetani(id); }

    /**
     * Simple mock chatbot — Gemini API integration can be added later.
     * Returns a helpful response based on keyword matching.
     */
    public ChatbotLog askChatbot(Petani petani, String prompt) {
        String response = generateMockResponse(prompt);
        
        ChatbotLog log = ChatbotLog.builder()
            .petani(petani)
            .userPrompt(prompt)
            .geminiResponse(response)
            .timestamp(LocalDateTime.now())
            .build();
        return chatbotLogRepository.save(log);
    }

    private String generateMockResponse(String prompt) {
        String lower = prompt.toLowerCase();
        if (lower.contains("hama") || lower.contains("wereng")) {
            return "Untuk mengatasi hama wereng, Anda bisa menggunakan pestisida organik berbahan dasar nimba. " +
                   "Selain itu, pastikan pengairan sawah diatur dengan baik (intermitten) untuk mengurangi populasi wereng. " +
                   "Jika serangan sudah parah, konsultasikan dengan PPL (Penyuluh Pertanian Lapangan) di daerah Anda.";
        } else if (lower.contains("pupuk") || lower.contains("kcl")) {
            return "Dosis pupuk KCL yang direkomendasikan untuk padi adalah 100-150 kg/ha. " +
                   "Aplikasikan pada umur 21 HST (Hari Setelah Tanam) bersamaan dengan pupuk Urea dan SP-36. " +
                   "Pastikan kondisi lahan lembab saat pemupukan untuk penyerapan optimal.";
        } else if (lower.contains("cuaca") || lower.contains("musim")) {
            return "Berdasarkan data BMKG, prakiraan cuaca minggu ini menunjukkan curah hujan sedang hingga lebat " +
                   "di beberapa wilayah. Disarankan untuk memperkuat drainase sawah dan menunda penyemprotan pestisida " +
                   "hingga cuaca cerah. Pantau terus informasi cuaca dari BMKG.";
        } else if (lower.contains("padi") || lower.contains("beras")) {
            return "Varietas padi unggul yang direkomendasikan saat ini antara lain: IR64, Ciherang, Inpari 32, " +
                   "dan Mekongga. Pilih varietas sesuai kondisi lahan dan ketinggian daerah Anda. " +
                   "Untuk dataran rendah, Ciherang dan Inpari 32 memberikan hasil optimal.";
        } else {
            return "Terima kasih atas pertanyaan Anda! Berdasarkan analisis AI, berikut saran untuk pertanian Anda: " +
                   "Pastikan untuk selalu memantau kondisi tanaman secara berkala, perhatikan pola cuaca, " +
                   "dan konsultasikan dengan petugas penyuluh pertanian setempat untuk hasil yang optimal. " +
                   "Apakah ada pertanyaan lebih spesifik yang bisa saya bantu?";
        }
    }
}
