package com.lab4.database_demo.controller;

import com.lab4.database_demo.controller.dtos.CarRequest;
import com.lab4.database_demo.controller.dtos.CarResponse;
import com.lab4.database_demo.controller.mapper.BrandMapper;
import com.lab4.database_demo.controller.mapper.CarMapper;
import com.lab4.database_demo.domain.Car;
import com.lab4.database_demo.service.AuthenticationService;
import com.lab4.database_demo.service.BrandService;
import com.lab4.database_demo.service.CarService;
import com.lab4.database_demo.service.impl.JWTService;
import com.lab4.database_demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class CarControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CarController carController;

    @MockBean
    private CarService carService;

    @MockBean
    private CarMapper carMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private BrandService brandService;

    @MockBean
    private BrandMapper brandMapper;

    @BeforeEach()
    public void setup()
    {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "test", roles = "CLIENT")
    void getCar_test() throws Exception {

        Car car = new Car();
        car.setModel("Ateca");

        CarResponse carResponse = new CarResponse();
        carResponse.setModel("Ateca");

        when(carService.getCarById(1)).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(carResponse);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Ateca"));
    }

    @Test
    @WithMockUser(username = "test", roles = "CLIENT")
    void getCars_test() throws Exception {

        Car car = new Car();
        car.setModel("Ateca");
        Car car2 = new Car();
        car2.setModel("Ateca2");
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car2);
        CarResponse carResponse1 = new CarResponse();
        carResponse1.setModel("Ateca");

        CarResponse carResponse2 = new CarResponse();
        carResponse2.setModel("Ateca2");

        CompletableFuture<List<Car>> futureCarList = CompletableFuture.completedFuture(cars);


        when(carService.getAllCars()).thenReturn(futureCarList);
        when(carMapper.toResponse(car)).thenReturn(carResponse1);
        when(carMapper.toResponse(car2)).thenReturn(carResponse2);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/findAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].model").value("Ateca"))
                .andExpect(jsonPath("$[1].model").value("Ateca2"));
    }

    @Test
    @WithMockUser(username = "test", roles = "VENDOR")
    void deleteById_success() throws Exception {
        int carId = 1;

        doNothing().when(carService).deleteById(carId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = "VENDOR")
    void updateCar_test() throws Exception {

        int carId = 1;
        CarRequest carRequest = new CarRequest();
        carRequest.setModel("Ateca");
        Car updatedCar = new Car();
        updatedCar.setModel("Ateca");

        when(carService.updateById(carId,carMapper.toModel(carRequest))).thenReturn(updatedCar);


        this.mockMvc.perform(MockMvcRequestBuilders.put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"model\":\"Ateca\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = "VENDOR")
    void addCar_test() throws Exception {

        CarRequest carRequest = new CarRequest();
        carRequest.setModel("Ateca");
        Car savedCar = new Car();
        savedCar.setModel("Ateca");

        when(carService.saveCar(carMapper.toModel(carRequest))).thenReturn(savedCar);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cars/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"model\":\"Ateca\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
