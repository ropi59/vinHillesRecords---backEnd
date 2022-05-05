package fr.insy2s.commerce.dtos.member;

import fr.insy2s.commerce.models.Role;
import lombok.*;

import java.util.regex.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    /**
     * Check if this DTO has valid values, meaning:
     * - first_name is not null and match the provided regex pattern (namePattern)
     * - last_name is not null and match the provided regex pattern (namePattern)
     * - email is not null and match the provided regex pattern (emailPattern)
     * - password is not null and not blank
     * @param namePattern regex pattern for first_name and last_name
     * @param emailPattern regex pattern for email
     * @return true if the DTO is valid
     */
    public boolean isValid(Pattern namePattern, Pattern emailPattern){
        return firstName!=null && lastName!=null && password!=null && email!= null && role!= null && !password.isBlank()
                && namePattern.matcher(firstName).find() && namePattern.matcher(lastName).find() && emailPattern.matcher(email).find();
    }
}
