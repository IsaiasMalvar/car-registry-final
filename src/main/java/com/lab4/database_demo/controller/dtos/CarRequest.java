package com.lab4.database_demo.controller.dtos;

import lombok.Data;

@Data
public class CarRequest {
    private BrandRequest brand;

    private String model;

    private int mileage;

    private double price;

    private int year;

    private String description;

    private String colour;

    private String fuelType;

    private int numDoors;
}
