package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.member.MemberCreationDTO;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.dtos.member.MemberDeleteDTO;
import fr.insy2s.commerce.dtos.member.MemberUpdateDTO;
import fr.insy2s.commerce.models.Member;

import java.util.List;

public interface MemberServiceInterface {

    public List<MemberDTO> findAllMembers ();

    public MemberDTO findMemberById (Long id);

    public MemberDTO createMember(MemberCreationDTO memberCreationDTO);

    public Member editMember(MemberUpdateDTO memberToUpdate, Long id);

    public Member deleteMember(MemberDeleteDTO memberToDelete, Long id);
}
