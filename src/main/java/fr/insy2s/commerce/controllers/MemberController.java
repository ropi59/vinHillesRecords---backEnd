package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.member.MemberCreationDTO;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.dtos.member.MemberDeleteDTO;
import fr.insy2s.commerce.dtos.member.MemberUpdateDTO;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost/")
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    /**
     * Method to get all members
     * @return list of all members
     */
    @GetMapping()
    public List<MemberDTO> findAllMembers(){
        return this.memberService.findAllMembers();
    }

    /**
     * Find a member by his id and sent info back
     * @param id member's id
     * @return MemberDTO
     */
    @GetMapping("{id}")
    public ResponseEntity<MemberDTO> findMemberById(@PathVariable Long id) {
        ResponseEntity<MemberDTO> result;
        try {
            MemberDTO memberDTO = this.memberService.findMemberById(id);
            if (memberDTO != null)
                result = ResponseEntity.ok(memberDTO);
            else
                result = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to create a member in database
     * @param memberCreationDTO member information to save
     * @return MemberDTO
     */
    @PostMapping()
    public ResponseEntity<MemberDTO> createMember(@RequestBody MemberCreationDTO memberCreationDTO){
        ResponseEntity<MemberDTO> result;
        try {
            MemberDTO memberDTO = this.memberService.createMember(memberCreationDTO);
            if(memberDTO != null)
                result = ResponseEntity.status(HttpStatus.CREATED).body(memberDTO);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to anonymize member in database
     * @param memberToDelete member's information to delete
     * @return member
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Member> deleteMember(@RequestBody MemberDeleteDTO memberToDelete, @PathVariable Long id){
        ResponseEntity<Member> result;
        try{
            Member member = this.memberService.deleteMember(memberToDelete, id);
            if(member != null)
                result = ResponseEntity.status(HttpStatus.OK).body(member);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to update member's information in database
     * @param memberToUpdate information to update
     * @param id member's id
     * @return member updated
     */
    @PutMapping("{id}")
    public ResponseEntity<Member> updateMember(@RequestBody MemberUpdateDTO memberToUpdate, @PathVariable Long id){
        ResponseEntity<Member> result;
        try {
            Member member = this.memberService.editMember(memberToUpdate, id);
            if (member != null)
                result = ResponseEntity.status(HttpStatus.OK).body(member);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }
}
