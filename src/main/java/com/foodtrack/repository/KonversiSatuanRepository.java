package com.foodtrack.repository;

import com.foodtrack.entity.KonversiSatuan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KonversiSatuanRepository extends JpaRepository<KonversiSatuan, Integer> {
    List<KonversiSatuan> findByKomoditasIdKomoditas(Integer idKomoditas);
}
