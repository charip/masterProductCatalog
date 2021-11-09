package com.catalog.entities;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

public class Supplier {

	@Getter @Setter
    @CsvBindByName(column="ID")
    private String id;

    @CsvBindByName(column="Name")
    private String name;

    @Override
    public String toString() {
        return "Supplier{" +
                "ID='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }   
    
}
