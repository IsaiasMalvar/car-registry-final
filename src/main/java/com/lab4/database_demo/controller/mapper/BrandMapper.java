package com.lab4.database_demo.controller.mapper;

import com.lab4.database_demo.controller.dtos.BrandRequest;
import com.lab4.database_demo.controller.dtos.BrandResponse;
import com.lab4.database_demo.domain.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class BrandMapper {
    public BrandResponse toResponse(Brand brand){
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setName(brand.getName());
        brandResponse.setCountry(brand.getCountry());
        brandResponse.setWarranty(brand.getWarranty());

        return brandResponse;
    }

    public Brand toModel (BrandRequest brand){

        Brand brandModel = new Brand();
        brandModel.setName(brand.getName());
        brandModel.setCountry(brand.getCountry());
        brandModel.setWarranty(brand.getWarranty());

        return brandModel;
    }
}
