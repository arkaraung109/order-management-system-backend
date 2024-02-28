package com.java.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetailPriceDto implements Serializable {

    private Long id;

    private Long price;

    private LocalDate updatedDate;

    private ProductDto product;

}
