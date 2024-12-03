package com.lab4.database_demo.repository;

import com.lab4.database_demo.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarEntity, Integer> {



}
