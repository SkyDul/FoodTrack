package com.foodtrack.repository;

import com.foodtrack.entity.Satuan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SatuanRepository extends JpaRepository<Satuan, Integer> {
    List<Satuan> findByKomoditasIdKomoditas(Integer idKomoditas);
    List<Satuan> findByKomoditasIdKomoditasAndIsBaseUnitTrue(Integer idKomoditas);
}
