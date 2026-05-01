package com.foodtrack.repository;

import com.foodtrack.entity.Petani;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PetaniRepository extends JpaRepository<Petani, Integer> {
    Optional<Petani> findByUsername(String username);
}
