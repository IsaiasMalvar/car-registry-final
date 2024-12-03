package com.lab4.database_demo.service.converter;
import com.lab4.database_demo.domain.Car;
import com.lab4.database_demo.entity.CarEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarConverter {

    @Autowired
    private BrandConverter brandConverter;

    public Car toCar (CarEntity entity){
        Car car = new Car();
        car.setBrand(brandConverter.toBrand(entity.getBrand()));
        car.setModel(entity.getModel());
        car.setYear(entity.getYear());
        car.setColour(entity.getColour());
        car.setMileage(entity.getMileage());
        car.setDescription(entity.getDescription());

        return car;
    }

    public CarEntity toEntity (Car car){
        CarEntity carEntity = new CarEntity();
        carEntity.setBrand(brandConverter.toEntity(car.getBrand()));
        carEntity.setModel(car.getModel());
        carEntity.setYear(car.getYear());
        carEntity.setColour(car.getColour());
        carEntity.setMileage(car.getMileage());
        carEntity.setDescription(car.getDescription());

        return carEntity;
    }
}
