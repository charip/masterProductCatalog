package com.catalog.entities;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter @NoArgsConstructor
public class Product {

	
	@CsvBindByName(column="SupplierID")
    private String supplierID;

	@CsvBindByName(column="SKU")
    private String sku;

    @CsvBindByName(column="Barcode")
    private String barcode;

    private String company;

    @Override
    public String toString() {
        return "Product{" +
        		"Supplier='" + supplierID + '\''+
                "SKU='" + sku + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }

	public Product (String supplierID, String sku, String barcode, String company) {
		this.sku = sku;
		this.supplierID = supplierID;
		this.barcode = barcode;
		this.company = company;
	}

    
    
}
