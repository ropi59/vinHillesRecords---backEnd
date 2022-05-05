package fr.insy2s.commerce.dtos.product;

import fr.insy2s.commerce.models.Tag;
import lombok.*;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDtoNoId {
  private String title;
  private String artist;
  private String description;
  private float price;
  private String label;
  private String format;
  private int stock;
  private String coverUrl;
  private Instant releaseDate;
  private Instant disponibilityDate;
  private List<Tag> tags;
}
