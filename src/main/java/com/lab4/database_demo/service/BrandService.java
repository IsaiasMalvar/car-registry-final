package com.lab4.database_demo.service;

import com.lab4.database_demo.controller.dtos.BrandRequest;
import com.lab4.database_demo.domain.Brand;
import com.lab4.database_demo.entity.BrandEntity;
import com.lab4.database_demo.service.exceptions.BadRequestException;
import com.lab4.database_demo.service.exceptions.NotFoundException;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface BrandService {

    Brand getBrandById(int id);

    void deleteBrandById(int id) ;

    Brand addBrand(Brand brandRequest) throws BadRequestException;

    Brand updateBrand(int id, Brand brandRequest) throws BadRequestException, NotFoundException;

    CompletableFuture<List<Brand>> getAllBrands();

}
