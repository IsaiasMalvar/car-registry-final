package com.lab4.database_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="brand")
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private int warranty;

    private String country;

    @OneToMany(mappedBy = "brand")
    List<CarEntity> car;


@Override
public String toString() {
    return "BrandEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            // Avoid including the cars directly
            "}";
}


}
