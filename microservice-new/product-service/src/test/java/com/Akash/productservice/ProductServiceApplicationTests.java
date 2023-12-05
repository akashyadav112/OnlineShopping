package com.Akash.productservice;

import com.Akash.productservice.dto.ProductRequest;
import com.Akash.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers // so junit understands that we are going to use test containers here
@AutoConfigureMockMvc // to autoconfigure mockMvc
class ProductServiceApplicationTests {


	@Autowired
	private MockMvc MockMvc; // this will be used to make request to product controller

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private  ProductRepository productRepository;
	@Container // so that junit understand that is a mongodb container,  docker image of mongodb 4.0.10 will be downloaded
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	// this method will assign the spring.data.mongodb.uri the url of docker image of mongodb url
	@DynamicPropertySource // this will add this property dynamically as time of running test
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	// To test with testcontainers.
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProduct();

		String productRequestString  = objectMapper.writeValueAsString(productRequest);
		MockMvc.perform(MockMvcRequestBuilders.post("api/products/add") // this is the api i want to hit via mockMVc
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))  // passing json as a string
				.andExpect(status().isCreated()); // this is my expected response

		Assertions.assertEquals(1, productRepository.findAll().size()); // means dockers image ke db ka size 1 ho jaana chaiye
	}

	private ProductRequest getProduct(){
		return ProductRequest.builder()
				.name("Activa")
				.description("modle is 3G")
				.price(BigDecimal.valueOf(90000))
				.build();
	}

}
