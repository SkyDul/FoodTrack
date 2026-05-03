package com.foodtrack.service;

import com.foodtrack.entity.HargaKomoditas;
import com.foodtrack.repository.HargaKomoditasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HargaKomoditasService {
    private final HargaKomoditasRepository hargaKomoditasRepository;

    public List<HargaKomoditas> findAll() { return hargaKomoditasRepository.findAll(); }
    public Optional<HargaKomoditas> findById(Integer id) { return hargaKomoditasRepository.findById(id); }
    public HargaKomoditas save(HargaKomoditas h) { return hargaKomoditasRepository.save(h); }
    public void deleteById(Integer id) { hargaKomoditasRepository.deleteById(id); }
    public List<HargaKomoditas> findByKomoditasId(Integer idKomoditas) { return hargaKomoditasRepository.findByKomoditasIdKomoditas(idKomoditas); }
}
