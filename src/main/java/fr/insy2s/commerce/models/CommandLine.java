package fr.insy2s.commerce.models;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@Table(name= "command_line")
@NoArgsConstructor
@AllArgsConstructor
public class CommandLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "command_line_id")
    private Long id;
    @Column (name = "unite_price")
    private float unitePrice;
    private int quantity;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @OneToOne
    @JoinColumn (name = "command_id")
    private Command command;
}
