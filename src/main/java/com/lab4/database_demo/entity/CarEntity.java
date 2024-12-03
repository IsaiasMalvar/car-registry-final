package com.lab4.database_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "car")
public class CarEntity {



    @Id
    @GeneratedValue

    private int id;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    private String model;

    private Integer mileage;

    private Double price;

    private Integer year;

    private String description;

    private String colour;

    private String fuelType;

    private Integer numDoors;

    @Override
    public String toString() {
        return "CarEntity{" +
                "id=" + id +
                ", colour='" + colour + '\'' +
                ", description='" + description + '\'' +
                ", mileage=" + mileage +
                // Avoid including the brand directly to prevent circular reference
                "}";
    }
}

