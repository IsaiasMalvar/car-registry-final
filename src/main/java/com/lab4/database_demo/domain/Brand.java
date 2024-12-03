package com.lab4.database_demo.domain;

import com.lab4.database_demo.entity.CarEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    private int id;

    private String name;

    private int warranty;

    private String country;

    List<CarEntity> car;
}
