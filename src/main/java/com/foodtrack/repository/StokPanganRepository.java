package com.foodtrack.repository;

import com.foodtrack.entity.StokPangan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StokPanganRepository extends JpaRepository<StokPangan, Integer> {
    List<StokPangan> findByPetaniIdPetani(Integer idPetani);
    List<StokPangan> findTop5ByOrderByCreatedAtDesc();
}
