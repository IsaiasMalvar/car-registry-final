package com.lab4.database_demo.controller.dtos;

import com.lab4.database_demo.entity.CarEntity;
import lombok.Data;

import java.util.List;

@Data
public class BrandRequest {

    private String name;

    private int warranty;

    private String country;
}
