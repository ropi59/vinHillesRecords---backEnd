package fr.insy2s.commerce.dtos.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandDto {

    private Long id;
    private Instant commandDate;
    private Long status;
    private Instant deliveryDate;
    private Long memberId;
    private Long adressId;
}
