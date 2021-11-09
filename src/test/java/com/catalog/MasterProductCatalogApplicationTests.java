package com.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ArchUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.catalog.entities.Catalog;
import com.catalog.entities.NewCatalog;
import com.catalog.entities.Product;

@SpringBootTest
class MasterProductCatalogApplicationTests {

	ClassLoader loader=MasterProductCatalogApplication.class.getClassLoader();
	MasterProductCatalogApplication testClass=new MasterProductCatalogApplication();
	
	@Test
	void contextLoads() {
		
	}
	
	@Test
	void testMasterCatalogGenerate(){
				
		List<Product> listA = new ArrayList<Product>();
		listA.add(new Product("01","sku1A","barcode1","A"));
		listA.add(new Product("01","sku1A","barcode2","A"));
		listA.add(new Product("01","sku1A","barcode3","A"));
		listA.add(new Product("02","sku2A","barcode4","A"));
		listA.add(new Product("02","sku2A","barcode5","A"));
		listA.add(new Product("03","sku3A","barcode6","A"));
		listA.add(new Product("03","sku3A","barcode7","A"));
		
		List<Product> listB = new ArrayList<Product>();
		listB.add(new Product("01","sku1B","barcode1","B"));
		listB.add(new Product("01","sku1B","barcode2","B"));
		listA.add(new Product("01","sku1A","barcode11","A"));
		
		listB.add(new Product("01","sku1B","barcode8","B"));
		listB.add(new Product("02","sku2B","barcode4","B"));
		listB.add(new Product("02","sku2B","barcode5","B"));
		listB.add(new Product("03","sku3A","barcode6","B"));
		listB.add(new Product("03","sku3A","barcode7","B"));
		
		Map<String, String> catalog= new HashedMap();
		catalog.put("sku1A","desc1");
		catalog.put("sku2A","desc2");
		catalog.put("sku3A","desc3");
		catalog.put("sku1B","desc1");
		catalog.put("sku2B","desc2");
		
				
		List<NewCatalog>finalList= testClass.produceMasterCatalog(listA, listB, catalog);
		
		assertNotNull(finalList);
		assertEquals(4, finalList.size());
		
		
	}
	
	@Test
	void testGenerateMasterCatalogList() {
		
		List<Catalog> catalogA= new ArrayList<Catalog>();
		catalogA.add(new Catalog("sku1","desc1"));
		catalogA.add(new Catalog("sku2","desc2"));
		catalogA.add(new Catalog("sku3","desc3"));
		
		List<Catalog> catalogB= new ArrayList<Catalog>();
		catalogB.add(new Catalog("sku3","desc3"));
		catalogB.add(new Catalog("sku2","desc2"));
		catalogB.add( new Catalog("sku4","desc1"));
		
		Map<String, String> newCat= testClass.generateMasterCatalogList(catalogA, catalogB);
		assertNotNull(newCat);
		assertEquals(4 ,newCat.size());
	}
	
	
}
