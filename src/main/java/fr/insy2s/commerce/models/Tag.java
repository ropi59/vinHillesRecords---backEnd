package fr.insy2s.commerce.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "tag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "tag_id")
    private Long id;
    private String name;

   @ManyToMany(mappedBy = "tags")
   @JsonIgnore
   List<Product> products;
}