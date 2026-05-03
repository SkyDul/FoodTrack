package com.foodtrack.service;

import com.foodtrack.entity.KonversiSatuan;
import com.foodtrack.repository.KonversiSatuanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KonversiSatuanService {
    private final KonversiSatuanRepository konversiSatuanRepository;

    public List<KonversiSatuan> findAll() { return konversiSatuanRepository.findAll(); }
    public Optional<KonversiSatuan> findById(Integer id) { return konversiSatuanRepository.findById(id); }
    public KonversiSatuan save(KonversiSatuan k) { return konversiSatuanRepository.save(k); }
    public void deleteById(Integer id) { konversiSatuanRepository.deleteById(id); }
    public List<KonversiSatuan> findByKomoditasId(Integer idKomoditas) { return konversiSatuanRepository.findByKomoditasIdKomoditas(idKomoditas); }
}
