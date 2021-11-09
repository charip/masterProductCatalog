package com.catalog.entities;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class Catalog {

	@CsvBindByName(column="SKU")
	private String sku;

	@CsvBindByName(column="Description")
	private String description;

	public Catalog (String sku, String description) {
		this.sku = sku;
		this.description = description;
		
	}
	@Override
	public String toString() {
		return "Catalog{" +
				"SKU='" + sku + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Catalog other = (Catalog) obj;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (!sku.equals(other.sku))
            return false;
        return true;
    }

}
