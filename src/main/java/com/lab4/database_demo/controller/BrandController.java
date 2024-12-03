package com.lab4.database_demo.controller;

import com.lab4.database_demo.controller.dtos.BrandRequest;
import com.lab4.database_demo.controller.dtos.BrandResponse;
import com.lab4.database_demo.controller.mapper.BrandMapper;
import com.lab4.database_demo.domain.Brand;
import com.lab4.database_demo.service.BrandService;
import com.lab4.database_demo.service.exceptions.BadRequestException;
import com.lab4.database_demo.service.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @Autowired
    public BrandController(BrandService brandService, BrandMapper brandMapper) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
    }

    @Operation(summary = "Obtain brand by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand found",content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BrandResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','VENDOR')")
    public ResponseEntity<Object> getBrand(@PathVariable int id){
        try {
            Brand brand = brandService.getBrandById(id);
            BrandResponse brandResponse = brandMapper.toResponse(brand);
            return ResponseEntity.ok(brandResponse);
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Create and store new Brand object", requestBody = @RequestBody(
            content = @Content(mediaType ="application/json", schema =  @Schema(implementation = BrandRequest.class))
    ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BrandResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error in the request")
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> addBrand(@RequestBody BrandRequest brandRequest){
        try{

            BrandResponse brandResponse = brandMapper.toResponse(brandService.addBrand(brandMapper.toModel(brandRequest)));
            return ResponseEntity.ok(brandResponse);
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete car from database", parameters = @Parameter(
            name = "id",
            description = "Brand ID",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer")
    ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car removed from database based on its id"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> deleteBrand(@PathVariable int id){
        try{
            brandService.deleteBrandById(id);
            return ResponseEntity.ok().body("Brand with ID" + id + " successfully removed.");
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Update existing brand object. Locates it through ID", requestBody = @RequestBody(
            content = @Content(mediaType ="application/json", schema =  @Schema(implementation = BrandRequest.class))
    ), parameters = @Parameter(
            name = "id",
            description = "Brand ID",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BrandResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Brand not found"),
            @ApiResponse(responseCode = "500", description = "Error in the request")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> updateBrand (@RequestBody BrandRequest brandRequest, @PathVariable int id){
        try{
            BrandResponse brandResponse = brandMapper.toResponse(brandService.updateBrand(id,brandMapper.toModel(brandRequest)));
            return ResponseEntity.ok(brandResponse);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
