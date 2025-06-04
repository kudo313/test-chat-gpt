package com.example.backend.service;

import com.example.backend.entity.CatKpi;
import com.example.backend.repository.CatKpiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatKpiService {
    private final CatKpiRepository repository;

    public CatKpiService(CatKpiRepository repository) {
        this.repository = repository;
    }

    public List<CatKpi> findAll() {
        return repository.findAll();
    }

    public Optional<CatKpi> findById(Integer id) {
        return repository.findById(id);
    }

    public CatKpi save(CatKpi catKpi) {
        return repository.save(catKpi);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
