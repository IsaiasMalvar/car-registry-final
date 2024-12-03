package com.lab4.database_demo.service.converter;

import com.lab4.database_demo.domain.Brand;
import com.lab4.database_demo.entity.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {

    public Brand toBrand(BrandEntity entity){
        Brand brand = new Brand();
        brand.setName(entity.getName());
        brand.setCountry(entity.getCountry());
        brand.setWarranty(entity.getWarranty());

        return brand;
    }

    public BrandEntity toEntity (Brand brand){

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName(brand.getName());
        brandEntity.setCountry(brand.getCountry());
        brandEntity.setWarranty(brand.getWarranty());

        return brandEntity;
    }
}
