package com.lab4.database_demo.service.converters;

import com.lab4.database_demo.domain.Brand;
import com.lab4.database_demo.domain.Car;
import com.lab4.database_demo.entity.BrandEntity;
import com.lab4.database_demo.entity.CarEntity;
import com.lab4.database_demo.service.converter.BrandConverter;
import com.lab4.database_demo.service.converter.CarConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarConverterTest {

    @InjectMocks
    private CarConverter carConverter;

    @Mock
    private BrandConverter brandConverter;

    @Test
    void toCar_test(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("renault");
        CarEntity entity = new CarEntity();
        entity.setId(1);
        entity.setYear(2000);
        entity.setMileage(10000);
        entity.setModel("clio");
        entity.setBrand(brandEntity);
        entity.setColour("red");
        entity.setDescription("great car");

        Brand brand = new Brand();
        brand.setName("renault");

        Car car = new Car();
        car.setYear(2000);
        car.setMileage(10000);
        car.setModel("clio");
        car.setBrand(brand);
        car.setColour("red");
        car.setDescription("great car");
        //when
        when(brandConverter.toBrand(brandEntity)).thenReturn(brand);
        //Then

        Car result = carConverter.toCar(entity);
        assertEquals(result.getYear(), car.getYear());
        assertEquals(result.getBrand(), car.getBrand());
        assertEquals(result.getColour(), car.getColour());
        assertEquals(result.getModel(), car.getModel());
        assertEquals(result.getMileage(), car.getMileage());
        assertEquals(result.getDescription(), car.getDescription());

    }
}
