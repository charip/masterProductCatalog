package com.catalog;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.catalog.entities.Catalog;
import com.catalog.entities.NewCatalog;
import com.catalog.entities.Product;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
public class MasterProductCatalogApplication {

	private static final String BARCODE_A_CSV= "input/barcodesA.csv";
	private static final String BARCODE_B_CSV= "input/barcodesB.csv";
	private static final String CATALOG_A_CSV= "input/catalogA.csv";
	private static final String CATALOG_B_CSV= "input/catalogB.csv";
	private static final String OUTPUT_CSV= "output/newCatalogOutput.csv";


	public static void main(String[] args) throws IOException, URISyntaxException {
		readCsvAndGenerateMergedCatalog();

	}

	private static void readCsvAndGenerateMergedCatalog() throws IOException, URISyntaxException {


		ClassLoader loader=MasterProductCatalogApplication.class.getClassLoader();
		List<Product> productListA = new CsvToBeanBuilder<Product>(new FileReader( Paths.get(loader.getResource(BARCODE_A_CSV).toURI()).toFile()))
				.withType(Product.class)
				.build()
				.parse();
		productListA.forEach(p->p.setCompany("A"));

		List<Product> productListB = new CsvToBeanBuilder<Product>(new FileReader( Paths.get(loader.getResource(BARCODE_B_CSV).toURI()).toFile()))
				.withType(Product.class)
				.build()
				.parse();
		productListB.forEach(p->p.setCompany("B"));

		List<Catalog> catalogListA = new CsvToBeanBuilder<Catalog>(new FileReader( Paths.get(loader.getResource(CATALOG_A_CSV).toURI()).toFile()))
				.withType(Catalog.class)
				.build()
				.parse();

		List<Catalog> catalogListB = new CsvToBeanBuilder<Catalog>(new FileReader( Paths.get(loader.getResource(CATALOG_B_CSV).toURI()).toFile()))
				.withType(Catalog.class)
				.build()
				.parse();

		// Generate final merged catalog output
		List<NewCatalog> finalCatalogList= generateMasterCatalog(productListA,productListB,catalogListA,catalogListB);

		// write to output file
		generateOutputCSV(loader, finalCatalogList);

	}

	public static void generateOutputCSV(ClassLoader loader, List<NewCatalog> finalCatalogList) throws IOException, URISyntaxException{

		try(Writer fileWriter= new FileWriter( Paths.get(loader.getResource(OUTPUT_CSV).toURI()).toFile())){

			HeaderColumnNameMappingStrategy<NewCatalog> strategy = new
					HeaderColumnNameMappingStrategy<>(); strategy.setType(NewCatalog.class);

					StatefulBeanToCsv<NewCatalog> sbc = new StatefulBeanToCsvBuilder<NewCatalog>(fileWriter)
							.withMappingStrategy(strategy) .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
							.build();

					sbc.write(finalCatalogList);

		}
		catch(Exception e) {
			e.printStackTrace();
		}


	}

	private static List<NewCatalog> generateMasterCatalog(List<Product> productListA, List<Product> productListB,
			List<Catalog> catalogListA, List<Catalog> catalogListB) {

		//Generate master catalog list
		Map<String, String> catalogSuperlist = generateMasterCatalogList(catalogListA,catalogListB); 
		//log.info("MasterCatalogList generated");


		//Generate master product list and transform to the final catalog list
		return produceMasterCatalog(productListA,productListB,catalogSuperlist);


	}


	public static List<NewCatalog> produceMasterCatalog(List<Product> productListA, List<Product> productListB,
			Map<String, String> catalogSuperlist) {

		//get a list of products from company B that already exists in A
		List<Product> duplicateListinB=new ArrayList<>();
		productListA.forEach(pA ->
		productListB.stream()
		.filter(pB -> pA.getBarcode().equals(pB.getBarcode()) && 
				pA.getSupplierID().equals(pB.getSupplierID()))
		.forEach(duplicateListinB::add));

		// Remove duplicate products from company B
		productListB.removeAll(duplicateListinB);

		//get a list of products from company A that already exists in B

		List<Product> duplicateListinA=new ArrayList<>();
		productListB.forEach(pB ->
		productListA.stream()
		.filter(pA -> pB.getBarcode().equals(pA.getBarcode()) && 
				pA.getSupplierID().equals(pB.getSupplierID()))
		.forEach(duplicateListinA::add));

		//remove common/duplicate products 
		productListA.removeAll(duplicateListinA);

		return Stream.concat(productListA.stream(), productListB.stream())
				.filter(distinctByKeys(Product::getSku, Product::getCompany))
				.map(m->new NewCatalog(m.getSku(),catalogSuperlist.get(m.getSku()) ,m.getCompany()))
				.collect(Collectors.toList());
	}

	public static Map<String, String> generateMasterCatalogList(List<Catalog> catalogListA,
			List<Catalog> catalogListB) {
		return Stream.concat(catalogListA.stream(),
				catalogListB.stream()).distinct().collect(
						Collectors.toMap(Catalog::getSku, Catalog::getDescription));
	}



	private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) 
	{
		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> 
		{
			final List<?> keys = Arrays.stream(keyExtractors)
					.map(ke -> ke.apply(t))
					.collect(Collectors.toList());

			return seen.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}
}
