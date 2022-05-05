package fr.insy2s.commerce.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "status_id")
    private Long id;
    @Column (name = "status_name")
    private String statusName;
}