package com.lab4.database_demo.controller.mappers;

import com.lab4.database_demo.controller.BrandController;
import com.lab4.database_demo.controller.dtos.BrandRequest;
import com.lab4.database_demo.controller.dtos.BrandResponse;
import com.lab4.database_demo.controller.mapper.BrandMapper;
import com.lab4.database_demo.controller.mapper.CarMapper;
import com.lab4.database_demo.domain.Brand;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
 class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrandController brandController;

    @MockBean
    private BrandService brandService;

    @MockBean
    private BrandMapper brandMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private CarService carService;

    @MockBean
    private CarMapper carMapper;

    @BeforeEach()
    public void setup()
    {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "test", roles = "CLIENT")
    void getBrand_test() throws Exception {
        Brand brand = new Brand();
        brand.setName("Toyota");

        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setName("Toyota");

        when(brandService.getBrandById(1)).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/brands/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Toyota"));
    }

    @Test
    @WithMockUser(username = "test", roles = "VENDOR")
    void addBrand_test() throws Exception {
        BrandRequest brandRequest = new BrandRequest();

        brandRequest.setName("Toyota");

        Brand brand = new Brand();
        brand.setName("Toyota");


        when(brandService.addBrand(brandMapper.toModel(brandRequest))).thenReturn(brand);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/brands/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Toyota\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser (username = "test" , roles = "VENDOR")
    void deleteBrand_test() throws Exception {
        int brandId = 1;

        doNothing().when(brandService).deleteBrandById(brandId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/brands/1").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = "VENDOR")
    void updateBrand_test() throws Exception {

        int brandId = 1;
        BrandRequest brandRequest = new BrandRequest();
        brandRequest.setName("Toyota");
        Brand updatedBrand = new Brand();
        updatedBrand.setName("Toyota");

        when(brandService.updateBrand(brandId,brandMapper.toModel(brandRequest))).thenReturn(updatedBrand);


        this.mockMvc.perform(MockMvcRequestBuilders.put("/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Toyota\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

