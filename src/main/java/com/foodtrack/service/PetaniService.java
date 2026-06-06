package com.foodtrack.service;

import com.foodtrack.entity.*;
import com.foodtrack.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor


/**
 * Kelas Service untuk PetaniService.
 * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.
 */
public class PetaniService {
    private final PetaniRepository petaniRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public List<Petani> findAll() { return petaniRepository.findAll(); }
    public Optional<Petani> findById(Integer id) { return petaniRepository.findById(id); }
    public Optional<Petani> findByUsername(String username) { return petaniRepository.findByUsername(username); }
    
    @Transactional
    public Petani save(Petani petani) { 
        // Encode password if it's new or being changed (check for BCrypt prefixes $2a$, $2b$, $2y$)
        if (petani.getPassword() != null && 
            !(petani.getPassword().startsWith("$2a$") || 
              petani.getPassword().startsWith("$2b$") || 
              petani.getPassword().startsWith("$2y$"))) {
            petani.setPassword(passwordEncoder.encode(petani.getPassword()));
        }
        return petaniRepository.save(petani); 
    }
    
    public void deleteById(Integer id) { petaniRepository.deleteById(id); }
    public long count() { return petaniRepository.count(); }

    @Transactional
    public boolean changePassword(Integer idPetani, String oldPassword, String newPassword) {
        Optional<Petani> pOpt = petaniRepository.findById(idPetani);
        if (pOpt.isPresent()) {
            Petani p = pOpt.get();
            if (passwordEncoder.matches(oldPassword, p.getPassword())) {
                p.setPassword(passwordEncoder.encode(newPassword));
                petaniRepository.save(p);
                return true;
            }
        }
        return false;
    }
}

