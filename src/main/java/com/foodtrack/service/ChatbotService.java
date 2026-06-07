package com.foodtrack.service;

import com.foodtrack.entity.ChatbotLog;
import com.foodtrack.entity.Petani;
import com.foodtrack.repository.ChatbotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j


/**
 * Kelas Service untuk ChatbotService.
 * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.
 */
public class ChatbotService {
    private final ChatbotLogRepository chatbotLogRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${mistral.api.key:}")
    private String mistralApiKey;

    public List<ChatbotLog> findAll() { return chatbotLogRepository.findAll(); }
    public List<ChatbotLog> findByPetaniId(Integer id) { return chatbotLogRepository.findByPetaniIdPetaniOrderByTimestampAsc(id); }
    public List<ChatbotLog> findRecent() { return chatbotLogRepository.findTop5ByOrderByTimestampDesc(); }
    public void deleteById(Integer id) { chatbotLogRepository.deleteById(id); }

    @Transactional
    public void deleteByPetaniId(Integer id) { chatbotLogRepository.deleteByPetaniIdPetani(id); }

    /**
     * Integrates with Google Gemini API
     */
    public ChatbotLog askChatbot(Petani petani, String prompt) {
        // Fetch history and limit to last 3 conversations to save tokens
        List<ChatbotLog> allHistory = findByPetaniId(petani.getIdPetani());
        List<ChatbotLog> limitedHistory = allHistory.size() > 3 
            ? allHistory.subList(allHistory.size() - 3, allHistory.size()) 
            : allHistory;

        String response = callGeminiApi(prompt, limitedHistory);
        
        ChatbotLog log = ChatbotLog.builder()
            .petani(petani)
            .userPrompt(prompt)
            .geminiResponse(response)
            .timestamp(LocalDateTime.now())
            .build();
        return chatbotLogRepository.save(log);
    }

    private String callGeminiApi(String prompt, List<ChatbotLog> history) {
        if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.equals("YOUR_API_KEY_HERE")) {
            log.warn("Gemini API Key is not set or using placeholder.");
            return getSimulatedResponse(prompt);
        }
        
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        StringBuilder promptWithContext = new StringBuilder();
        promptWithContext.append("Anda adalah Asisten Pakar Pertanian FoodTrack. Anda ahli dalam ketahanan pangan Indonesia, teknik budidaya tanaman lokal, dan solusi ramah lingkungan. ");
        promptWithContext.append("Jawablah dalam Bahasa Indonesia yang profesional, empatik, dan praktis. Fokuslah pada solusi yang bisa diterapkan petani di lahan mereka.\n\n");
        
        if (history != null && !history.isEmpty()) {
            promptWithContext.append("Riwayat Percakapan Terakhir (Gunakan sebagai konteks):\n");
            for (ChatbotLog h : history) {
                promptWithContext.append("Petani: ").append(h.getUserPrompt()).append("\n");
                promptWithContext.append("Asisten: ").append(h.getGeminiResponse()).append("\n\n");
            }
        }
        promptWithContext.append("Pertanyaan Petani Saat Ini: ").append(prompt);

        Map<String, Object> parts = new HashMap<>();
        parts.put("text", promptWithContext.toString());
        
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(parts));
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> body = responseEntity.getBody();
            if (body != null && body.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> candidateContent = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> resParts = (List<Map<String, Object>>) candidateContent.get("parts");
                    if (resParts != null && !resParts.isEmpty()) {
                        return (String) resParts.get(0).get("text");
                    }
                }
            }
            log.error("Gemini API returned unexpected response structure: {}", body);
            return getSimulatedResponse(prompt);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            log.error("Gemini API HTTP Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 429) {
                log.warn("Gemini API rate limit exceeded (429). Attempting fallback to Mistral API...");
                return callMistralApi(prompt, promptWithContext.toString());
            }
            return "Maaf, asisten AI sedang mengoptimalkan jawaban. Sebagai saran ahli FoodTrack: " + getSimulatedResponse(prompt);
        } catch (Exception e) {
            log.error("Gemini API General Error ({}): {}", e.getClass().getSimpleName(), e.getMessage());
            return "Maaf, asisten AI sedang beristirahat sejenak. Sebagai saran ahli FoodTrack: " + getSimulatedResponse(prompt);
        }
    }

    private String callMistralApi(String originalPrompt, String promptWithContext) {
        if (mistralApiKey == null || mistralApiKey.isEmpty() || mistralApiKey.equals("<ISI DENGAN API MISTRAL>")) {
            log.warn("Mistral API Key is not set. Falling back to simulated response.");
            return "Maaf, kuota AI harian telah habis. Sebagai saran ahli FoodTrack: " + getSimulatedResponse(originalPrompt);
        }

        String url = "https://api.mistral.ai/v1/chat/completions";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(mistralApiKey);
        
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", promptWithContext);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "open-mistral-nemo");
        requestBody.put("messages", List.of(message));
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> body = responseEntity.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    if (messageObj != null && messageObj.containsKey("content")) {
                        return (String) messageObj.get("content");
                    }
                }
            }
            log.error("Mistral API returned unexpected response structure: {}", body);
            return "Maaf, kuota AI harian telah habis. Sebagai saran ahli FoodTrack: " + getSimulatedResponse(originalPrompt);
        } catch (Exception e) {
            log.error("Mistral API Error ({}): {}", e.getClass().getSimpleName(), e.getMessage());
            return "Maaf, kuota AI harian telah habis. Sebagai saran ahli FoodTrack: " + getSimulatedResponse(originalPrompt);
        }
    }

    private String getSimulatedResponse(String prompt) {
        String p = prompt.toLowerCase();
        
        // Priority 1: Specific Pest/Disease (Must be checked first)
        if (p.contains("hama") || p.contains("penyakit") || p.contains("ulat") || p.contains("wereng") || p.contains("jamur") || p.contains("bercak") || p.contains("busuk") || p.contains("kuning")) {
            if (p.contains("jamur") || p.contains("bercak") || p.contains("busuk")) {
                return "Gejala penyakit (seperti blast pada padi, antraknosa pada cabai, atau busuk akar) biasanya ditandai dengan bercak atau area lunak pada tanaman. Langkah awal: kurangi kelembapan lahan, pastikan drainase lancar, dan gunakan fungisida nabati (misal: ekstrak bawang putih atau kunyit).";
            }
            if (p.contains("wereng") || p.contains("kuning")) {
                return "Waspadai hama wereng cokelat yang bisa menularkan virus kerdil rumput/hampa (daun menguning). Segera keringkan lahan secara berkala (irigasi berselang) dan gunakan agens hayati seperti Beauveria bassiana.";
            }
            return "Untuk pengendalian hama/penyakit, gunakan prinsip PHT (Pengendalian Hama Terpadu). Pantau lahan secara rutin dan gunakan pestisida nabati (daun mimba/gadung) sebagai langkah preventif sebelum menggunakan bahan kimia.";
        }
        
        // Priority 2: Fertilization
        if (p.contains("pupuk") || p.contains("subur") || p.contains("kompos")) {
            return "Untuk tanah Indonesia, disarankan menggunakan pemupukan berimbang. Gunakan pupuk organik (kompos/pukandang) untuk memperbaiki struktur tanah, dikombinasikan dengan NPK sesuai fase pertumbuhan. Hindari ketergantungan kimia berlebih agar tanah tetap subur jangka panjang.";
        }
        
        // Priority 3: Water/Climate
        if (p.contains("air") || p.contains("irigasi") || p.contains("kemarau") || p.contains("hujan") || p.contains("iklim") || p.contains("cuaca")) {
            if (p.contains("hujan") || p.contains("banjir")) {
                return "Di musim hujan, pastikan sistem drainase di lahan lancar untuk mencegah pembusukan akar. Jika curah hujan sangat tinggi, kurangi dosis pupuk Nitrogen agar tanaman tidak mudah terserang jamur.";
            }
            return "Pantau selalu data BMKG. Di musim kemarau, gunakan metode irigasi berselang (intermittent) pada padi untuk menghemat air sekaligus memperkuat akar tanaman.";
        }
        
        // Priority 4: Harvest/Market (General crop names)
        if (p.contains("panen") || p.contains("harga") || p.contains("pasar") || p.contains("untung") || p.contains("jual")) {
            return "Pastikan memanen saat kematangan optimal (90% gabah menguning untuk padi). Untuk harga terbaik, pantau harga pasar melalui FoodTrack atau jual melalui koperasi kelompok tani untuk memperkuat posisi tawar Anda.";
        }
        
        if (p.contains("tanam") || p.contains("bibit") || p.contains("unggul") || p.contains("padi") || p.contains("jagung")) {
            return "Gunakan benih bersertifikat (seperti Inpari untuk padi). Benih unggul memiliki daya tahan lebih baik terhadap perubahan iklim dan serangan penyakit lokal di lahan Indonesia.";
        }

        return "Sebagai mitra digital Anda, FoodTrack menyarankan untuk selalu menjaga kualitas hasil panen dan kebersihan lahan. Pertanian yang cerdas dimulai dari pencatatan data yang baik di aplikasi ini. Ada hal spesifik lain yang ingin Anda konsultasikan?";
    }
}

