package com.lab4.database_demo.service.converters;

import com.lab4.database_demo.domain.Brand;
import com.lab4.database_demo.entity.BrandEntity;
import com.lab4.database_demo.service.converter.BrandConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BrandConverterTest {

    @InjectMocks
    private BrandConverter brandConverter;

    @Test
    void toBrand_test() {
        BrandEntity entity = new BrandEntity();
        entity.setName("Renault");
        entity.setCountry("France");
        Brand brand = new Brand();
        brand.setName("Renault");
        brand.setCountry("France");



        Brand result = brandConverter.toBrand(entity);
        assertEquals(brand.getCountry(),result.getCountry());
        assertEquals(brand.getName(), result.getName());

    }
}
