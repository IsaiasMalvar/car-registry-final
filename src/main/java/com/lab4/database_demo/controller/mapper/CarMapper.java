package com.lab4.database_demo.controller.mapper;

import com.lab4.database_demo.controller.dtos.CarRequest;
import com.lab4.database_demo.controller.dtos.CarResponse;
import com.lab4.database_demo.domain.Car;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public class CarMapper {

    @Autowired
    private BrandMapper brandMapper;

    public CarResponse toResponse (Car carRequest){
        CarResponse car = new CarResponse();
        car.setBrand(brandMapper.toResponse(carRequest.getBrand()));
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setColour(carRequest.getColour());
        car.setMileage(carRequest.getMileage());
        car.setDescription(carRequest.getDescription());
        car.setFuelType(carRequest.getFuelType());

        return car;
    }

    public Car toModel (CarRequest car){
        Car carModel = new Car();
        carModel.setBrand(brandMapper.toModel(car.getBrand()));
        carModel.setModel(car.getModel());
        carModel.setYear(car.getYear());
        carModel.setColour(car.getColour());
        carModel.setMileage(car.getMileage());
        carModel.setDescription(car.getDescription());
        carModel.setFuelType(car.getFuelType());

        return carModel;
    }
}
