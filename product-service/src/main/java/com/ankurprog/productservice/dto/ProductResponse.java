package com.ankurprog.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String productName;
    private String productCategory;
    private String brand;
    private String description;
    private Double price;
    private String currency;
    private LocalDate releaseDate;
    private String origin;
    private String battery;
    private Boolean touchId;
    private Boolean exchangePossible;
    private Double displaySize;
    private Boolean faceID;
}
