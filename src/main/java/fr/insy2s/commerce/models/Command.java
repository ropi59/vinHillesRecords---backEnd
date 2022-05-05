package fr.insy2s.commerce.models;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name= "command")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "command_id")
    private Long id;

    @Column (name = "command_date")
    private Instant commandDate;
    @OneToOne
    @JoinColumn (name = "status_id")
    private Status status;

    @Column (name = "delivery_date")
    private Instant deliveryDate;
    @OneToOne
    @JoinColumn (name = "member_id")
    private Member member;
    @OneToOne
    @JoinColumn (name = "adress_id")
    private Adress adress;

    public Command(Instant commandDate, Status status, Instant deliveryDate, Member member, Adress adress) {
        this.commandDate = commandDate;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.member = member;
        this.adress = adress;
    }
}
