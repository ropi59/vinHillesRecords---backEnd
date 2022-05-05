package fr.insy2s.commerce.dtos.member;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Long roleId;
}
