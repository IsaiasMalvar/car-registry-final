package com.lab4.database_demo.service.impl;

import com.lab4.database_demo.controller.dtos.BrandRequest;
import com.lab4.database_demo.domain.Brand;
import com.lab4.database_demo.entity.BrandEntity;
import com.lab4.database_demo.repository.BrandRepository;
import com.lab4.database_demo.service.BrandService;
import com.lab4.database_demo.service.converter.BrandConverter;
import com.lab4.database_demo.service.exceptions.BadRequestException;
import com.lab4.database_demo.service.exceptions.NotFoundException;
import com.lab4.database_demo.service.exceptions.utils.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandConverter brandConverter;

    @Autowired
    private BrandRepository brandRepository;


    @Override
    public Brand getBrandById(int id) {
        Optional<BrandEntity> brandEntityOptional = brandRepository.findById(id);

        if (brandEntityOptional.isPresent()) {
            return brandConverter.toBrand(brandEntityOptional.get());
        } else {
            throw new NotFoundException(ErrorMessages.BRAND_NOT_FOUND.getMessage());
        }
    }

    @Override
    public void deleteBrandById(int id) throws NotFoundException {
        Optional<BrandEntity> brandEntityOptional = brandRepository.findById(id);

        if (brandEntityOptional.isPresent()) {
            brandRepository.deleteById(id);
        } else {
            throw new NotFoundException(ErrorMessages.BRAND_NOT_FOUND.getMessage());
        }
    }

    @Override
    public Brand addBrand(Brand brandRequest) throws BadRequestException {

        try {
           BrandEntity brandEntity = brandConverter.toEntity(brandRequest);
           return brandConverter.toBrand(brandRepository.save(brandEntity));
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessages.INVALID_BRAND_DATA.getMessage(), e);
        }
    }

    @Override
    public Brand updateBrand(int id, Brand brandRequest) throws BadRequestException, NotFoundException {
        try {

            if (!brandRepository.existsById(id)) {
                throw new NotFoundException(ErrorMessages.BRAND_NOT_FOUND.getMessage());
            }

            BrandEntity updatedBrandEntity = brandConverter.toEntity(brandRequest);
            updatedBrandEntity.setId(id);

            BrandEntity savedEntity = brandRepository.save(updatedBrandEntity);
            return brandConverter.toBrand(savedEntity);

        } catch (NotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new BadRequestException(ErrorMessages.INVALID_BRAND_DATA.getMessage(), e);
        }
    }
    @Override
    public CompletableFuture<List<Brand>> getAllBrands() throws NotFoundException {
        try{
            List<BrandEntity> brandEntityList = (brandRepository.findAll());
            List<Brand> brandList = new ArrayList<>();

            for (BrandEntity brandEntity : brandEntityList) {
                Brand brand = brandConverter.toBrand(brandEntity);
                brandList.add(brand);
            }
            return CompletableFuture.completedFuture(brandList);
        }catch (Exception e){
            throw new NotFoundException();
        }
    }
}
