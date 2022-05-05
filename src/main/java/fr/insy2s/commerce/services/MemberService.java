package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.MemberMappers;
import fr.insy2s.commerce.dtos.member.MemberCreationDTO;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.dtos.member.MemberDeleteDTO;
import fr.insy2s.commerce.dtos.member.MemberUpdateDTO;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.RegexCollection;
import fr.insy2s.commerce.repositories.MemberRepository;
import fr.insy2s.commerce.services.interfaces.MemberServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberServiceInterface {

    private final MemberRepository memberRepository;
    private final RegexCollection regexCollection;

    /**
     * Method to get all members
     * @return list of memberDTO
     */
    @Override
    public List<MemberDTO> findAllMembers() {
        //get all members
        List<Member> membersList;
        membersList = this.memberRepository.findAll();

        List<MemberDTO> result;
        result = membersList.stream().map(MemberMappers::memberToMemberDto).collect(Collectors.toList());
        return result;
    }

    /**
     * Search a member by id
     * @param id member's id
     * @return memberDTO
     */
    @Override
    public MemberDTO findMemberById(Long id) {
        MemberDTO result;
        if (id != null ){
            Optional<Member> optionalMember = memberRepository.findMemberById(id);
            result = optionalMember.map(MemberMappers::memberToMemberDto).orElse(null);
        }
        else
            result = null;
        return result;
    }

    /**
     * Method to save a member entity into database
     * @param memberCreationDTO member's information
     * @return memberDTO
     */
    @Override
    public MemberDTO createMember(MemberCreationDTO memberCreationDTO) {
        MemberDTO result = null;
        if (memberCreationDTO != null && memberCreationDTO.isValid(regexCollection.getNamePattern(), regexCollection.getEmailPattern())) {
            Member member = this.memberRepository.save(MemberMappers.creationDTOToMember(memberCreationDTO));
            result = MemberMappers.memberToMemberDto(member);
        }
        return result;
    }

    /**
     * Update existing member data
     * @param memberToUpdate member data to update
     * @return member updated
     * @throws NoSuchElementException if user is not found
     * @throws IllegalArgumentException if data aren't valid
     * @throws NullPointerException if user's id not exist
     */
    @Override
    public Member editMember(MemberUpdateDTO memberToUpdate, Long id)
            throws NoSuchElementException, IllegalArgumentException {
        //Find member to update
        Member member = this.memberRepository.findMemberById(id).orElse(null);
        if (member == null){
            throw new NoSuchElementException("Member update : Unknown member");
        }
        //Check data
        if (!memberToUpdate.isValid(regexCollection.getNamePattern(), regexCollection.getEmailPattern())){
            throw new IllegalArgumentException ("Member update: Invalid parameter data");
        } else {
            //Update member
            member = this.memberRepository.save(MemberMappers.updateDTOToMember(memberToUpdate, id));
        }
        return member;
    }

    /**
     * Method to anonymize member's data
     * @param memberToDelete member to anonymize
     * @return member
     * @throws NoSuchElementException if user is not found
     */
    @Override
    public Member deleteMember(MemberDeleteDTO memberToDelete, Long id)
    throws NoSuchElementException {
        //Find member to anonymize
        Member member = this.memberRepository.findMemberById(id).orElse(null);
        if (member == null){
            throw new NoSuchElementException("Member delete : Unknown member");
        }
        //Anonymize member's data
        member.setFirstName("deletedMemberFirstName");
        member.setLastName("deletedMemberLastName");
        member.setPhoneNumber(null);
        member.setEmail("deletedMember@mail.com");
        member.setAvatar("default");
        member.setIsActive(false);
        //Update member's data
        this.memberRepository.save(member);
        return member;
    }
}
