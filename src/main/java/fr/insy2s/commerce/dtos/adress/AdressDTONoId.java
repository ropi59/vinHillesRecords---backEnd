package fr.insy2s.commerce.dtos.adress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdressDTONoId {

    private String streetNumber;
    private String streetName;
    private String zipCode;
    private String city;
    private String country;
    private List<Long> membersIds;

    /**
     * Check if this DTO has valid values, meaning:
     * @return true if the DTO is valid
     */
    public boolean isValid(){
        return streetNumber !=null && streetName !=null && zipCode!=null && city!= null && country!= null && membersIds != null
                && !streetNumber.isBlank() && !streetName.isBlank() && !zipCode.isBlank() && !city.isBlank() && !country.isBlank() && !membersIds.isEmpty();
    }
}
