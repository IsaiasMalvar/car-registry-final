package com.lab4.database_demo.repository;

import com.lab4.database_demo.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {

    Optional<BrandEntity> findByName(String name);
}
