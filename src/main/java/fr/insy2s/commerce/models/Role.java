package fr.insy2s.commerce.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "role_id")
    private Long id;
    @Column (name = "role_name")
    private String roleName;
}
