package com.foodtrack.repository;

import com.foodtrack.entity.Petani;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data PetaniRepository.
 */
public interface PetaniRepository extends JpaRepository<Petani, Integer> {
    Optional<Petani> findByUsername(String username);
}

