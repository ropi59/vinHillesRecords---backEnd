package fr.insy2s.commerce.dtos.member;

import fr.insy2s.commerce.models.Adress;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberContactDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<Adress> adressList = new ArrayList<>();
}
