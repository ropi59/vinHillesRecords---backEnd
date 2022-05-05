package fr.insy2s.commerce.dtos.member;

import fr.insy2s.commerce.models.Adress;
import lombok.*;

import java.util.List;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDeleteDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String avatar;
    private String resetKey;
    private List<Adress> adressList;

    public boolean isValid(Pattern namePattern, Pattern emailPattern){
        return firstName!=null && lastName!=null && password!=null && email!= null && !password.isBlank()
                && namePattern.matcher(firstName).find() && namePattern.matcher(lastName).find() && emailPattern.matcher(email).find();
    }
}
