package com.foodtrack.service;

import com.foodtrack.entity.Satuan;
import com.foodtrack.repository.SatuanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SatuanService {
    private final SatuanRepository satuanRepository;

    public List<Satuan> findAll() { return satuanRepository.findAll(); }
    public Optional<Satuan> findById(Integer id) { return satuanRepository.findById(id); }
    public Satuan save(Satuan s) { return satuanRepository.save(s); }
    public void deleteById(Integer id) { satuanRepository.deleteById(id); }
    public List<Satuan> findByKomoditasId(Integer idKomoditas) { return satuanRepository.findByKomoditasIdKomoditas(idKomoditas); }
}
