package com.foodtrack.repository;

import com.foodtrack.entity.HargaKomoditas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data HargaKomoditasRepository.
 */
public interface HargaKomoditasRepository extends JpaRepository<HargaKomoditas, Integer> {
    List<HargaKomoditas> findByKomoditasIdKomoditas(Integer idKomoditas);
}

