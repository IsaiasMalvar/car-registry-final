package com.lab4.database_demo.service;

import com.lab4.database_demo.domain.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CarService {
    Car getCarById(int id);

    void deleteById(int id);

    Car saveCar(Car carRequest);

    Car updateById(Integer id, Car carRequest);

    CompletableFuture<List<Car>> getAllCars();

    String carCsv();

    void uploadCars(MultipartFile file);

}
