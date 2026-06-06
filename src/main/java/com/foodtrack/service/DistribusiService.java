package com.foodtrack.service;

import com.foodtrack.entity.Distribusi;
import com.foodtrack.repository.DistribusiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor


/**
 * Kelas Service untuk DistribusiService.
 * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.
 */
public class DistribusiService {
    private final DistribusiRepository distribusiRepository;

    public List<Distribusi> findAll() { return distribusiRepository.findAll(); }
    public Optional<Distribusi> findById(Integer id) { return distribusiRepository.findById(id); }
    public List<Distribusi> findByPetaniId(Integer idPetani) { return distribusiRepository.findByStokPanganPetaniIdPetani(idPetani); }
    public Distribusi save(Distribusi d) { return distribusiRepository.save(d); }
    public void deleteById(Integer id) { distribusiRepository.deleteById(id); }
    public long count() { return distribusiRepository.count(); }
    public long countByStatus(String status) { return distribusiRepository.countByStatusPengiriman(status); }

    /** Advance status: Menunggu â†’ Dikirim â†’ Selesai */
    public void advanceStatus(Integer id) {
        distribusiRepository.findById(id).ifPresent(d -> {
            switch (d.getStatusPengiriman()) {
                case "Menunggu" -> {
                    d.setStatusPengiriman("Dikirim");
                    d.setTanggalKirim(LocalDate.now());
                }
                case "Dikirim" -> d.setStatusPengiriman("Selesai");
            }
            distribusiRepository.save(d);
        });
    }
}

