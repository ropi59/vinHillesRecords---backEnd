package fr.insy2s.commerce.dtos.commandline;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandLineDTO {

    private Long id;
    private float unitePrice;
    private int quantity;
    private Long productId;
    private Long commandId;

    /**
     * Check if DTO has valid values, meaning:
     * id is not null
     * unitePrice is > to 0
     * quantity is > 0
     * product id is not null
     * command id is not nul
     * @return true if DTO is valid
     */
    public boolean isValid(){
        return id != null && unitePrice > 0 && quantity > 0 && productId != null && commandId != null;
    }
}
