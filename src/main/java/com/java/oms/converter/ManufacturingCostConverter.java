package com.java.oms.converter;

import com.java.oms.entity.ManufacturingCost;
import org.modelmapper.AbstractConverter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManufacturingCostConverter extends AbstractConverter<List<ManufacturingCost>, Long> {

    @Override
    protected Long convert(List<ManufacturingCost> manufacturingCostList) {
        if(manufacturingCostList.isEmpty()) {
            return null;
        }
        return Collections.max(manufacturingCostList, Comparator.comparing(ManufacturingCost::getCreationTimestamp)).getCost();
    }

}
