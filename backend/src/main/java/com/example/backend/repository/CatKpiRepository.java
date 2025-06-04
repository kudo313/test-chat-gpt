package com.example.backend.repository;

import com.example.backend.entity.CatKpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatKpiRepository extends JpaRepository<CatKpi, Integer> {
}
