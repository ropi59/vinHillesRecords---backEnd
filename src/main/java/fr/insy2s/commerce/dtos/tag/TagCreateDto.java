package fr.insy2s.commerce.dtos.tag;

import fr.insy2s.commerce.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagCreateDto {
  private String name;
  List<Product> products;
}
