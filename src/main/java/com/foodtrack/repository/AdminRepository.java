package com.foodtrack.repository;

import com.foodtrack.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data AdminRepository.
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
}

