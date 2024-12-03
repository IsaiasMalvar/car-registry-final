package com.lab4.database_demo.controller;

import com.lab4.database_demo.controller.dtos.CarRequest;
import com.lab4.database_demo.controller.dtos.CarResponse;
import com.lab4.database_demo.controller.mapper.CarMapper;
import com.lab4.database_demo.domain.Car;
import com.lab4.database_demo.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequestMapping("/cars")
@Slf4j
@RestController
public class CarController {
    private final CarService carService;
    private final CarMapper carMapper;

    @Autowired
    public CarController(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    @Operation(summary = "Get car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','VENDOR')")
    public ResponseEntity<Object> getCar(@PathVariable int id){
        try{
            Car car = carService.getCarById(id);
            CarResponse carResponse = carMapper.toResponse(car);
            return ResponseEntity.ok(carResponse);
        }catch (Exception e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @Operation(summary = "Create and store new Car object", requestBody = @RequestBody(
            content = @Content(mediaType ="application/json", schema =  @Schema(implementation = CarRequest.class))
    ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car added successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Car could not be added")
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> addCar(@RequestBody CarRequest carRequest) {
        try {
            CarResponse response = carMapper.toResponse(carService.saveCar(carMapper.toModel(carRequest)));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all cars in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receive array with all available cars", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CarResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Cars not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> getCars() {
        try {
            log.info("Processing request in thread: {}", Thread.currentThread().getName());
            CompletableFuture<List<Car>> carsFuture = carService.getAllCars();
            List<Car> cars = carsFuture.get();

            if (cars == null || cars.isEmpty()) {
                log.info("No cars found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No cars available.");
            }

            List<CarResponse> responses = new ArrayList<>();
            cars.forEach(car -> responses.add(carMapper.toResponse(car)));
            return ResponseEntity.ok(responses);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            log.error("Thread was interrupted while processing getCars.", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (ExecutionException e) {
            log.error("ExecutionException occurred while processing getCars.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while processing getCars.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtain CSV file with all available cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attached CSV file with all available cars", content = {
                    @Content(mediaType = "text/csv")
            }),
    })
    @GetMapping(value = "/downloadCars")
    public ResponseEntity<Object> downloadCars () {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("cars.csv")
                .build());

        byte[] csvBytes = carService.carCsv().getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "Delete car from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car removed from database based on its id"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> deleteById(@PathVariable int id){
        log.info("Deleting car");
        try {
            carService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update existing car in database" , requestBody = @RequestBody(
            content = @Content(mediaType ="application/json", schema =  @Schema(implementation = CarRequest.class))
    ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated from database based on its id"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> updateCarById(@PathVariable int id, @RequestBody CarRequest carRequest){
        try{
            carService.updateById(id, carMapper.toModel(carRequest));
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Upload csv file to feed database.",
            description = "CSV for feeding cars to database.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "text/csv",
                            schema = @Schema(type = "string", description = "CSV file containing cars.")
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database updated with cars."),
            @ApiResponse(responseCode = "400", description = "Request error")
    })
    @PostMapping(value = "/uploadCsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<String> uploadCsv (@RequestParam(value = "file") MultipartFile file){

        if(file.isEmpty()) {
            log.error("The file is empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(Objects.requireNonNull(file.getOriginalFilename()).contains(".csv")){
            carService.uploadCars(file);
            return ResponseEntity.ok("File successfully uploaded.");
        }

        log.error("the file is not csv");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is not an CSV.");
    }

}
