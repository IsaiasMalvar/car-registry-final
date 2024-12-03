package com.lab4.database_demo.service.impl;

import com.lab4.database_demo.domain.Car;
import com.lab4.database_demo.entity.BrandEntity;
import com.lab4.database_demo.entity.CarEntity;
import com.lab4.database_demo.repository.BrandRepository;
import com.lab4.database_demo.repository.CarRepository;
import com.lab4.database_demo.service.CarService;
import com.lab4.database_demo.service.converter.CarConverter;
import com.lab4.database_demo.service.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarConverter carConverter;

    @Autowired
    private BrandRepository brandRepository;

    private static final List<String> header = new ArrayList<>(Arrays.asList(
            "price",
            "brand",
            "year",
            "model",
            "colour",
            "description",
            "mileage",
            "numDoors",
            "fuelType"
    ));


    @Override
    public Car getCarById(int id) {

        Optional<CarEntity> carOptional = carRepository.findById(id);
        return carOptional.map(entity -> carConverter.toCar(entity)).orElse(null);
    }

    @Override
    public void deleteById(int id) {

        log.info("Deleting car with id {}", id);

        if(carRepository.existsById(id)){
            carRepository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Car saveCar(Car carRequest) {

        log.info("Adding car to database");
        CarEntity entity = carConverter.toEntity(carRequest);
        return carConverter.toCar(carRepository.save(entity));
    }

    @Override
    public Car updateById(Integer id, Car carRequest) {
        log.info("Updating car with id {}", id);
        Optional<CarEntity> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            CarEntity entity = carConverter.toEntity(carRequest);
            entity.setId(id);
            return carConverter.toCar(carRepository.save(entity));
        }
        return null;
    }

    @Override
    @Async
    public CompletableFuture<List<Car>> getAllCars() {

        List<CarEntity> carList = carRepository.findAll();
        List<Car> cars = new ArrayList<>();
        carList.forEach(car -> cars.add(carConverter.toCar(car)));
        return CompletableFuture.completedFuture(cars);
    }

    @Override
    public String carCsv() {
        List<CarEntity> carList = carRepository.findAll();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(String.join(",", CarServiceImpl.header));
        csvContent.append("\n");

        for (CarEntity car : carList) {
            csvContent.append(car.getPrice()).append(",").append(car.getBrand().getName()).append(",")
                    .append(car.getYear()).append(",")
                    .append(car.getModel()).append(",")
                    .append(car.getColour()).append(",")
                    .append(car.getDescription()).append(",")
                    .append(car.getMileage()).append(",")
                    .append(car.getNumDoors()).append(",")
                    .append(car.getFuelType())
                    .append("\n");
        }
        return csvContent.toString();
    }

    @Override
    public void uploadCars(MultipartFile file) {
        List<CarEntity> carList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build())) {

            List<String> csvHeader = csvParser.getHeaderNames().stream().map( property -> property.replace(" ", "")).toList();

            if (!CarServiceImpl.header.equals(csvHeader)) {
                throw new RuntimeException("Invalid CSV header");
            }

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord recordForParsing : csvRecords){
                CarEntity carEntity = csvRecordToCarEntity(recordForParsing);
                carList.add(carEntity);
            }

            carRepository.saveAll(carList);

        } catch (IOException e){
            throw new RuntimeException("CSV could not be read");
        }
    }

    private CarEntity csvRecordToCarEntity(CSVRecord csvRecord) {

        CarEntity carEntity = new CarEntity();
        BrandEntity brandEntity = new BrandEntity();
        HashMap<String, String> mappedCsv = new HashMap<>();
        int idx = 0;

        for (String value : csvRecord.values()) {

            if (value.isEmpty()) {
                mappedCsv.put(CarServiceImpl.header.get(idx), "invalid");
            } else {
                mappedCsv.put(CarServiceImpl.header.get(idx), value);
            }
            idx++;
        }

        Set<Map.Entry<String, String>> entrySet = mappedCsv.entrySet();
        processEntrySet(entrySet, carEntity, brandEntity);
        return carEntity;
    }

    private void processEntrySet(Set<Map.Entry<String, String>> entrySet, CarEntity carEntity, BrandEntity brandEntity) {
        String intRegex = "^\\d+$";
        String doubleRegex = "^\\d+(\\.\\d+)?$";

        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            processField(key, value, carEntity, brandEntity, intRegex, doubleRegex);
        }

        resetUnmappedFields(carEntity);
    }

    private void processField(String key, String value, CarEntity carEntity, BrandEntity brandEntity, String intRegex, String doubleRegex) {
        switch (key) {
            case "price":
                carEntity.setPrice(parseDouble(value, doubleRegex));
                break;

            case "brand":
                processBrand(value, carEntity, brandEntity);
                break;

            case "year":
                carEntity.setYear(parseInteger(value, intRegex));
                break;

            case "model":
                carEntity.setModel(value);
                break;

            case "colour":
                carEntity.setColour(value);
                break;

            case "description":
                carEntity.setDescription(value);
                break;

            case "mileage":
                carEntity.setMileage(parseInteger(value, intRegex));
                break;

            case "numDoors":
                carEntity.setNumDoors(parseInteger(value, intRegex));
                break;

            case "fuelType":
                carEntity.setFuelType(value);
                break;

            default:
                break;
        }
    }

    private Double parseDouble(String value, String doubleRegex) {
        if (!value.matches(doubleRegex)) {
            return null;
        }
        return Double.parseDouble(value);
    }

    private Integer parseInteger(String value, String intRegex) {
        if (!value.matches(intRegex)) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private void processBrand(String value, CarEntity carEntity, BrandEntity brandEntity) {

        Optional<BrandEntity> availableBrand = brandRepository.findByName(value);

        if (availableBrand.isEmpty()) {
            throw new NotFoundException("Brand not found");
        } else {
            brandEntity.setName(value);
            carEntity.setBrand(availableBrand.get());
        }
    }

    private void resetUnmappedFields(CarEntity carEntity) {
        if (carEntity.getModel() == null) carEntity.setModel(null);
        if (carEntity.getMileage() == null) carEntity.setMileage(null);
        if (carEntity.getPrice() == null) carEntity.setPrice(null);
        if (carEntity.getYear() == null) carEntity.setYear(null);
        if (carEntity.getDescription() == null) carEntity.setDescription(null);
        if (carEntity.getColour() == null) carEntity.setColour(null);
        if (carEntity.getFuelType() == null) carEntity.setFuelType(null);
        if (carEntity.getNumDoors() == null) carEntity.setNumDoors(null);
    }

}



