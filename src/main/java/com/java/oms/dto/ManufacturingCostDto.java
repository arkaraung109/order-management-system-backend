package com.java.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManufacturingCostDto implements Serializable {

    private Long id;

    private Long cost;

    private LocalDateTime creationTimestamp;

    private ProductDto product;

}
