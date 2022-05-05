package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.member.MemberCreationDTO;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.dtos.member.MemberDeleteDTO;
import fr.insy2s.commerce.dtos.member.MemberUpdateDTO;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.Role;

public final class MemberMappers {

    private MemberMappers() {
        //Private constructor for sonarqube
    }

    /**
     * Convert an entity into memberDTO
     * @param member is an entity
     * @return a MemberDTO
     */
    public static MemberDTO memberToMemberDto(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setFirstName(member.getFirstName());
        memberDTO.setLastName(member.getLastName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setRoleId(member.getRole().getId());
        return memberDTO;
    }

    /**
     * Convert a memberDTO into member entity
     * @param memberDTO is a DTO of member
     * @return a member entity
     */
    public static Member memberDTOToMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setFirstName(memberDTO.getFirstName());
        member.setLastName(memberDTO.getLastName());
        member.setEmail(memberDTO.getEmail());
        return member;
    }
    /**
     * Convert a memberCreationDTO into member entity
     * @param memberCreationDTO is a DTO for create member
     * @return a member entity
     */
    public static Member creationDTOToMember(MemberCreationDTO memberCreationDTO) {
        Member member = new Member();
        Role role = new Role(3L, "customer");
        member.setFirstName(memberCreationDTO.getFirstName());
        member.setLastName(memberCreationDTO.getLastName());
        member.setEmail(memberCreationDTO.getEmail());
        member.setPassword(memberCreationDTO.getPassword());
        member.setRole(role);
        member.setIsActive(true);
        return member;
    }

    /**
     * Convert a memberUpdateDTO into member entity
     * @param memberUpdateDTO is a DTO for update member information
     * @return a member entity
     */
    public static Member updateDTOToMember(MemberUpdateDTO memberUpdateDTO, Long id) {
        Member member = new Member();
        Role role = new Role(3L, "customer");
        member.setId(id);
        member.setFirstName(memberUpdateDTO.getFirstName());
        member.setLastName(memberUpdateDTO.getLastName());
        member.setEmail(memberUpdateDTO.getEmail());
        member.setPhoneNumber(memberUpdateDTO.getPhoneNumber());
        member.setPassword(memberUpdateDTO.getPassword());
        member.setAvatar(memberUpdateDTO.getAvatar());
        member.setAdressList(memberUpdateDTO.getAdressList());
        member.setResetKey(memberUpdateDTO.getResetKey());
        member.setIsActive(true);
        member.setRole(role);
        return member;
    }

    /**
     * Convert a member into a memberUpdateDTO
     * @param member is a member got in database
     * @return a memberUpdateDTO
     */
    public static MemberUpdateDTO memberToUpdateDto(Member member) {
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setFirstName(member.getFirstName());
        memberUpdateDTO.setLastName(member.getLastName());
        memberUpdateDTO.setEmail(member.getEmail());
        memberUpdateDTO.setPhoneNumber(member.getPhoneNumber());
        memberUpdateDTO.setPassword(member.getPassword());
        memberUpdateDTO.setAvatar(member.getAvatar());
        memberUpdateDTO.setAdressList(member.getAdressList());
        return memberUpdateDTO;
    }

    /**
     * anonymize member information's
     * @param member to anonymize
     * @return a member with no personal information
     */
    public static MemberDeleteDTO memberToDeleteDto(Member member) {
        MemberDeleteDTO memberDeleteDTO = new MemberDeleteDTO();
        memberDeleteDTO.setFirstName(member.getFirstName());
        memberDeleteDTO.setLastName(member.getLastName());
        memberDeleteDTO.setEmail(member.getEmail());
        memberDeleteDTO.setPhoneNumber(member.getPhoneNumber());
        memberDeleteDTO.setPassword(member.getPassword());
        memberDeleteDTO.setAvatar(member.getAvatar());
        memberDeleteDTO.setAdressList(member.getAdressList());
        return memberDeleteDTO;
    }
}

