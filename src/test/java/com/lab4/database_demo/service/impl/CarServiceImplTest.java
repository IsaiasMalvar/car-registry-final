package com.lab4.database_demo.service.impl;

import com.lab4.database_demo.domain.Car;
import com.lab4.database_demo.entity.BrandEntity;
import com.lab4.database_demo.entity.CarEntity;
import com.lab4.database_demo.repository.CarRepository;
import com.lab4.database_demo.service.converter.CarConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarConverter carConverter;


    @Test
    void getCarById_test(){

        //given
        Optional<CarEntity> entity = Optional.of(new CarEntity());
        entity.get().setId(1);
        Car car = new Car();

        //when
        when(carRepository.findById(1)).thenReturn(entity);
        when(carConverter.toCar(entity.get())).thenReturn(car);

        //then
        Car result = carService.getCarById(1);
        assertEquals(result,car);
    }

    @Test
    void getCarById_test_ko(){
        //given

        //when

        when(carRepository.findById(1)).thenReturn(Optional.empty());

        //then
        Car result = carService.getCarById(1);
        assertNull(result);
    }

    @Test
    void deleteById_test(){

        //given
        int carId = 1;
        //when
        when(carRepository.existsById(carId)).thenReturn(true);
        carService.deleteById(carId);
        //then
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    void saveCar_test(){

        //given
        CarEntity entity = new CarEntity();
        entity.setColour("red");
        Car car = new Car();
        car.setColour("red");

        //when
        when(carConverter.toEntity(car)).thenReturn(entity);
        when(carConverter.toCar(entity)).thenReturn(car);
        when(carRepository.save(entity)).thenReturn(entity);

        //then
        Car result = carService.saveCar(car);
        assertEquals(result, car);
    }

    @Test
    void updateById_test(){

        //given
        Optional<CarEntity> entity = Optional.of(new CarEntity());
        entity.get().setId(2);
        Car car = new Car();
        car.setColour("yellow");
        entity.get().setColour("yellow");

        //when
        when(carRepository.findById(2)).thenReturn(entity);
        when(carConverter.toEntity(car)).thenReturn(entity.get());
        when(carRepository.save(entity.get())).thenReturn(entity.get());
        when(carConverter.toCar(entity.get())).thenReturn(car);

        //then
        Car result = carService.updateById(2, car);
        assertEquals(result, car);
    }

    @Test
    void getAllCars_test() throws ExecutionException, InterruptedException {

        //given
        CarEntity entity1 = new CarEntity();
        CarEntity entity2 = new CarEntity();
        entity1.setId(3);
        entity2.setId(4);
        entity1.setYear(2000);
        entity2.setYear(2001);
        List<CarEntity> entityList = new ArrayList<>();
        entityList.add(entity1);
        entityList.add(entity2);
        List<Car> carList = new ArrayList<>();
        entityList.forEach(entity -> {
            Car car = new Car();
            car.setYear(entity.getYear());
            carList.add(car);
        });

        //when
        when(carRepository.findAll()).thenReturn(entityList);
        when(carConverter.toCar(entityList.get(0))).thenReturn(carList.get(0));
        when(carConverter.toCar(entityList.get(1))).thenReturn(carList.get(1));

        //then
        CompletableFuture<List<Car>> futureResult = carService.getAllCars();
        List<Car> result = futureResult.get();
        assertEquals(result, carList);
    }

    @Test
    void carCsv_test(){

        //given
        BrandEntity toyota = new BrandEntity();
        BrandEntity ford = new BrandEntity();
        toyota.setName("toyota");
        ford.setName("ford");
        CarEntity car1 = new CarEntity(5, toyota, "Corolla", 50000, 15000.00, 2020, "A reliable compact car", "Red", "Petrol", 4);
        CarEntity car2 = new CarEntity(6, ford, "Focus", 30000, 18000.00, 2021, "A sporty and fuel-efficient car", "Blue", "Diesel", 4);
        List<CarEntity> carEntityList = new ArrayList<>();
        Collections.addAll(carEntityList, car1, car2);
        String header = "price,brand,year,model,colour,description,mileage,numDoors,fuelType"+"\n";
        String car1String = "15000.0,toyota,2020,Corolla,Red,A reliable compact car,50000,4,Petrol"+"\n";
        String car2String = "18000.0,ford,2021,Focus,Blue,A sporty and fuel-efficient car,30000,4,Diesel"+"\n";
        String csv = header+car1String+car2String;

        //when
        when(carRepository.findAll()).thenReturn(carEntityList);

        //then
        String result = carService.carCsv();
        assertEquals(result, csv);
    }

}
