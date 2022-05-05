package fr.insy2s.commerce.models;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refund")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "refund_id")
    private Long id;
    private String status;
    @Column (name = "asking_date")
    private Instant askingDate;
    private String reason;
    @OneToOne
    @JoinColumn (name = "command_line_id")
    private CommandLine commandLine;
}
