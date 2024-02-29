package com.java.oms.converter;

import com.java.oms.entity.RetailPrice;
import org.modelmapper.AbstractConverter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RetailPriceConverter extends AbstractConverter<List<RetailPrice>, Long> {

    @Override
    protected Long convert(List<RetailPrice> retailPriceList) {
        if(retailPriceList.isEmpty()) {
            return null;
        }
        return Collections.max(retailPriceList, Comparator.comparing(RetailPrice::getCreationTimestamp)).getPrice();
    }

}