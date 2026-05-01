package com.foodtrack.repository;

import com.foodtrack.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Integer> {
    List<ChatbotLog> findByPetaniIdPetaniOrderByTimestampDesc(Integer idPetani);
    List<ChatbotLog> findTop5ByOrderByTimestampDesc();
    void deleteByPetaniIdPetani(Integer idPetani);
}
