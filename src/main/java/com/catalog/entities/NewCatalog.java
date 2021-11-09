package com.catalog.entities;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class NewCatalog {

	@CsvBindByName(column="SKU")
	private String sku;

	@CsvBindByName(column="Description")
	private String description;

	@CsvBindByName(column="Source")
	private String source;


	@Override
	public String toString() {
		return "Catalog{" +
				"SKU='" + sku + '\'' +
				", description='" + description + '\'' +
				", source='" + source + '\'' +
				'}';
	}


	public NewCatalog(String sku, String description, String company) {
		this.sku = sku;
		this.description = description;
		this.source = company;
	}

}
