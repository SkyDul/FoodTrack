package com.foodtrack.repository;

import com.foodtrack.entity.KonversiSatuan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data KonversiSatuanRepository.
 */
public interface KonversiSatuanRepository extends JpaRepository<KonversiSatuan, Integer> {
    List<KonversiSatuan> findByKomoditasIdKomoditas(Integer idKomoditas);
}

