package com.lab4.database_demo.controller.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lab4.database_demo.domain.Brand;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarResponse {
    private BrandResponse brand;

    private String model;

    private int mileage;

    private double price;

    private int year;

    private String description;

    private String colour;

    private String fuelType;

    private int numDoors;

    private String error;
}



