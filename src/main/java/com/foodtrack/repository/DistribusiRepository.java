package com.foodtrack.repository;

import com.foodtrack.entity.Distribusi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DistribusiRepository extends JpaRepository<Distribusi, Integer> {
    List<Distribusi> findByStatusPengiriman(String status);
    List<Distribusi> findByStokPanganPetaniIdPetani(Integer idPetani);
    long countByStatusPengiriman(String status);
}
