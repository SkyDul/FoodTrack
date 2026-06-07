package com.foodtrack.service;

import com.foodtrack.entity.Komoditas;
import com.foodtrack.repository.KomoditasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor


/**
 * Kelas Service untuk KomoditasService.
 * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.
 */
public class KomoditasService {
    private final KomoditasRepository komoditasRepository;

    public List<Komoditas> findAll() { return komoditasRepository.findAll(); }
    public Optional<Komoditas> findById(Integer id) { return komoditasRepository.findById(id); }
    public Komoditas save(Komoditas k) { return komoditasRepository.save(k); }
    public void deleteById(Integer id) { komoditasRepository.deleteById(id); }
    public long count() { return komoditasRepository.count(); }
}

