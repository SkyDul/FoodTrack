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
public class PetaniService {
    private final PetaniRepository petaniRepository;

    public List<Petani> findAll() { return petaniRepository.findAll(); }
    public Optional<Petani> findById(Integer id) { return petaniRepository.findById(id); }
    public Optional<Petani> findByUsername(String username) { return petaniRepository.findByUsername(username); }
    public Petani save(Petani petani) { return petaniRepository.save(petani); }
    public void deleteById(Integer id) { petaniRepository.deleteById(id); }
    public long count() { return petaniRepository.count(); }
}
