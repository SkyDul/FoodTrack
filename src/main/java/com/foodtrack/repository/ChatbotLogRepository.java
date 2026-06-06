package com.foodtrack.repository;

import com.foodtrack.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data ChatbotLogRepository.
 */
public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Integer> {
    List<ChatbotLog> findByPetaniIdPetaniOrderByTimestampAsc(Integer idPetani);
    List<ChatbotLog> findTop5ByOrderByTimestampDesc();
    void deleteByPetaniIdPetani(Integer idPetani);
}

