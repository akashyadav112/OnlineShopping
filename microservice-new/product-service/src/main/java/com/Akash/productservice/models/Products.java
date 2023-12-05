package com.Akash.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder // for building it(example we can convert entity to dto with help of builder)
@Data // for getter setter, to string etc
public class Products {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
