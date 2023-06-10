package com.ankurprog.productservice;

import com.ankurprog.productservice.dto.ProductRequest;
import com.ankurprog.productservice.dto.ProductResponse;
import com.ankurprog.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.hamcrest.Matchers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Tests
 *
 * @author ankur
 */
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    // 1.Integration Test for product creation
    @Test
    void shouldCreateProduct() throws Exception {
        List<ProductRequest> productRequestList = getProductRequest();
        String productRequesetString = objectMapper.writeValueAsString(productRequestList);
        String productListString = """
                			[{
                       "productName":"Samsung Galexy G Ultra",
                       "productCategory":"Mobile Phone",
                       "brand": "Samsung",
                       "description":"Samsung battery backup is 80 hours . ",
                       "price":1199.99,
                       "currency":"EUR",
                       "releaseDate":"2022-12-30",
                       "origin":"South Korea",
                       "battery":"9000Mh",
                       "touchId":"False",
                       "exchangePossible":"False",
                       "displaySize": 6.4,
                       "faceID":"True"
                   
                   }]
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productListString)
        ).andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    private List<ProductRequest> getProductRequest() {
        List<ProductRequest> productRequestList = new ArrayList<>();
        productRequestList.add(ProductRequest.builder()
                .productName("Samsung Galaxy 21")
                .productCategory("Mobile")
                .brand("Samsung")
                .battery("4200 mh")
                .origin("South Korea")
                .releaseDate(LocalDate.now())
                .price(1399.99)
                .description("Good phone with many features.")
                .displaySize(7.1)
                .exchangePossible(Boolean.FALSE)
                .touchId(Boolean.FALSE)
                .currency("EUR")
                .faceID(Boolean.TRUE)
                .build());
        return productRequestList;
    }

    // 2. Integration Test for getting all products
    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    // 3. Integration Test for getting all products by brand name
    @Test
    void testGetProductByBrand() throws Exception {
        String brandName="Samsung";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/brand/{brandName}",brandName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("Samsung"));

    }
    /**
    // 4. Integration Test for getting all products between two dates
    @Test
        void testGetProductBetweenDates () throws Exception {
            String startDate;
            mockMvc.perform(MockMvcRequestBuilders.get("/api/products/between-dates")
                            .param("startDate", LocalDate startDate)
                            .param("endDate", "2023-12-01"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
        }
    */


}
