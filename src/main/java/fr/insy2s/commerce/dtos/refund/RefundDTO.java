package fr.insy2s.commerce.dtos.refund;
import lombok.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundDTO {

    private Long id;
    private String status;
    private Instant askingDate;
    private String reason;
    private Long commandLineId;

    /**
     * Check if DTO has valid values, meaning:
     * id is not null
     * status is not null and not blank
     * askingDate is not null
     * reason is not null and not blank
     * commandLine is not null
     * @return true if DTO is valid
     */
    public boolean validFields() {
        return id != null && status != null && askingDate != null && reason != null && commandLineId != null && !reason.isBlank() && !status.isBlank();
    }
}
