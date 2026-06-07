package com.foodtrack.repository;

import com.foodtrack.entity.Komoditas;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data KomoditasRepository.
 */
public interface KomoditasRepository extends JpaRepository<Komoditas, Integer> {
}

