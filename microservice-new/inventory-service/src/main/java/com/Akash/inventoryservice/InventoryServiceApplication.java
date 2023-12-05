package com.Akash.inventoryservice;

import com.Akash.inventoryservice.model.Inventory;
import com.Akash.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	// this will be executed and these data will be updated in the db but why i do not need to autowired the inventory
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = new Inventory();
			Inventory inventory1 = new Inventory();
			inventory.setSkuCode("Active_3G");
			inventory.setQuantity(100);
			inventory1.setSkuCode("iPhone_13");
			inventory1.setQuantity(50);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
