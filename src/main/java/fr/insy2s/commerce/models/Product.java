package fr.insy2s.commerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name= "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "product_id")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;
    private String artist;
    private String description;

    private float price;
    private String label;
    private String format;
    private int stock;
    @Column (name = "cover_url")
    private String coverUrl;

    @Column (name = "release_date")
    private Instant releaseDate;

    @Column (name = "disponibility_date")
    private Instant disponibilityDate;

    @ManyToMany()
    @JoinTable(
            name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<Tag> tags;
}
