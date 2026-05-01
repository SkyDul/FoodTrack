package com.foodtrack.service;

import com.foodtrack.entity.StokPangan;
import com.foodtrack.repository.StokPanganRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StokPanganService {
    private final StokPanganRepository stokPanganRepository;

    public List<StokPangan> findAll() { return stokPanganRepository.findAll(); }
    public Optional<StokPangan> findById(Integer id) { return stokPanganRepository.findById(id); }
    public List<StokPangan> findByPetaniId(Integer idPetani) { return stokPanganRepository.findByPetaniIdPetani(idPetani); }
    public List<StokPangan> findRecent() { return stokPanganRepository.findTop5ByOrderByCreatedAtDesc(); }
    public StokPangan save(StokPangan s) { return stokPanganRepository.save(s); }
    public void deleteById(Integer id) { stokPanganRepository.deleteById(id); }
    public long count() { return stokPanganRepository.count(); }
}
