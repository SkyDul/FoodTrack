package com.foodtrack.repository;

import com.foodtrack.entity.HargaKomoditas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HargaKomoditasRepository extends JpaRepository<HargaKomoditas, Integer> {
    List<HargaKomoditas> findByKomoditasIdKomoditas(Integer idKomoditas);
}
