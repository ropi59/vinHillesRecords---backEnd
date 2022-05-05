package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.MemberMappers;
import fr.insy2s.commerce.dtos.member.MemberCreationDTO;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.dtos.member.MemberDeleteDTO;
import fr.insy2s.commerce.dtos.member.MemberUpdateDTO;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.RegexCollection;
import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.repositories.MemberRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTest {

    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private RegexCollection regexCollection;
    private MemberService memberService;

    @BeforeAll
    void init() {memberService = new MemberService(memberRepository, regexCollection);}

    //Convenient Method for testing

    private Member createValidMember(){
        Role role = new Role(3L, "customer");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("john");
        member.setLastName("doe");
        member.setEmail("johndoe@mail.com");
        member.setPassword("azerty");
        member.setRole(role);
        return member;
    }

    //Method findMemberById

    /**
     * Test findMemberById when member is found
     */
    @DisplayName("Test findMemberById : member found")
    @Test
    void testFindMemberByIdMemberFound() {
        Role role = new Role (3L, "customer");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("john");
        member.setLastName("doe");
        member.setEmail("johndoe@mail.com");
        member.setPassword("azerty");
        member.setRole(role);
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));
        MemberDTO result = this.memberService.findMemberById(member.getId());
        Assertions.assertNotNull(result);
        MemberDTO memberDTO = MemberMappers.memberToMemberDto(member);
        Assertions.assertEquals(memberDTO, result);
    }

    /**
     * Test findMemberById when member is not found
     */
    @DisplayName("Test findMemberById : member not found")
    @Test
    void testFindMemberByIdMemberNotFound() {
        Long id = 123456789L;
        BDDMockito.when(this.memberRepository.findMemberById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(this.memberService.findMemberById(id));
    }

    /**
     * Test findMemberById when id is invalid
     */
    @DisplayName("Test findMemberById : invalid id")
    @Test
    void testFindMemberByInvalidId(){
        Long id = 2992337203685477580L;
        Assertions.assertNull(this.memberService.findMemberById(id));
    }

    /**
     * Test findMemberById when id is null
     */
    @DisplayName("Test findMemberById : null id")
    @Test
    void testFindMemberByIdNullId(){
        Long id = null;
        Assertions.assertNull(this.memberService.findMemberById(id));
    }

    //Method createMember

    /**
     * Test createMember successfully
     */
    @DisplayName("Test createMember : creation successful")
    @Test
    void testCreateMemberSuccessful() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azerty", role);
        Member member = MemberMappers.creationDTOToMember(memberCreationDTO);
        BDDMockito.when(memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(member);
        MemberDTO result = this.memberService.createMember(memberCreationDTO);
        MemberDTO memberDTO = MemberMappers.memberToMemberDto(member);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(memberDTO, result);
    }

    /**
     * Test createMember when memberDTO is null
     */
    @DisplayName("Test createMember : memberCreationDTO null")
    @Test
    void testCreateMemberNullDTO() {
        MemberDTO memberDTO = Assertions.assertDoesNotThrow(() -> this.memberService.createMember(null));
        Assertions.assertNull(memberDTO);
    }

    /**
     * Test createMember when firstname is null
     */
    @DisplayName("Test createMember : firstName null")
    @Test
    void testCreateMemberFirstNameNull() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO(null, "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when lastname is null
     */
    @DisplayName("Test createMember : lastName null")
    @Test
    void testCreateMemberLastNameNull() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", null, "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when email is null
     */
    @DisplayName("Test createMember : email null")
    @Test
    void testCreateMemberEmailNull() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", null, "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when password is null
     */
    @DisplayName("Test createMember : password null")
    @Test
    void testCreateMemberPasswordNull() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "johndoe@mail.com", null, role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when role is null
     */
    @DisplayName("Test createMember : role null")
    @Test
    void testCreateMemberRoleNull() {
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azerty", null);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when FirstName is empty
     */
    @DisplayName("Test createMember : firstName empty")
    @Test
    void testCreateMemberEmptyFirstName() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when LastName is empty
     */
    @DisplayName("Test createMember : lastName empty")
    @Test
    void testCreateMemberEmptyLastName() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when email is empty
     */
    @DisplayName("Test createMember : email empty")
    @Test
    void testCreateMemberEmptyEmail() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when password is empty
     */
    @DisplayName("Test createMember : password empty")
    @Test
    void testCreateMemberEmptyPassword() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
    }

    /**
     * Test createMember when email is not valid
     */
    @DisplayName("Test createMember : email not valid")
    @Test
    void testCreateMemberInvalidEmail() {
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "johndoe@mail.", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
        MemberCreationDTO memberCreationDTO1 = new MemberCreationDTO("john", "doe", "johndoe.", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO1));
        MemberCreationDTO memberCreationDTO2 = new MemberCreationDTO("john", "doe", "johndoemail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO2));
        MemberCreationDTO memberCreationDTO3 = new MemberCreationDTO("john", "doe", "johndoe@.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO3));
    }

    /**
     * Test createMember with invalid firstName
     */
    @DisplayName("Test createMember : firstName not valid")
    @Test
    void testCreateMemberInvalidFirstName(){
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john5", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
        MemberCreationDTO memberCreationDTO1 = new MemberCreationDTO("john john", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO1));
        MemberCreationDTO memberCreationDTO2 = new MemberCreationDTO("john!", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO2));
        MemberCreationDTO memberCreationDTO3 = new MemberCreationDTO("johnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO3));
        MemberCreationDTO memberCreationDTO4 = new MemberCreationDTO("j", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO4));
        MemberCreationDTO memberCreationDTO5 = new MemberCreationDTO("johnéa", "doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO5));
    }

    /**
     * Test createMember with invalid lastName
     */
    @DisplayName("Test createMember : lastName not valid")
    @Test
    void testCreateMemberInvalidLastName(){
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe5", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO));
        MemberCreationDTO memberCreationDTO1 = new MemberCreationDTO("john", "doe doe", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO1));
        MemberCreationDTO memberCreationDTO2 = new MemberCreationDTO("john", "doe!", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO2));
        MemberCreationDTO memberCreationDTO3 = new MemberCreationDTO("john", "doeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO3));
        MemberCreationDTO memberCreationDTO4 = new MemberCreationDTO("john", "d", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO4));
        MemberCreationDTO memberCreationDTO5 = new MemberCreationDTO("john", "doée", "johndoe@mail.com", "azerty", role);
        Assertions.assertNull(this.memberService.createMember(memberCreationDTO5));
    }

    /**
     * Test createMember who already exist in database
     */
    @DisplayName("Test createMember : duplicate entry")
    @Test
    void testCreateMemberEmailAlreadyExist(){
        Role role = new Role(3L, "customer");
        MemberCreationDTO memberCreationDTO = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azerty", role);
        Member member = MemberMappers.creationDTOToMember(memberCreationDTO);
        BDDMockito.given(memberRepository.save(ArgumentMatchers.any(Member.class)))
                .willAnswer(inv -> {throw new SQLException("Duplicate entry");});
        Assertions.assertThrows(SQLException.class, () -> this.memberService.createMember(memberCreationDTO));
    }

    //Method updateMember

    /**
     * Test updateMember with new firstName
     */
    @DisplayName("Test updateMember : new firstName")
    @Test
    void testUpdateMemberWithNewFirstName(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setFirstName("johnny");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.editMember(memberUpdateDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

    /**
     * Test updateMember with new lastName
     */
    @DisplayName("Test updateMember : new lastName")
    @Test
    void testUpdateMemberWithNewLastName(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setLastName("dolittle");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.editMember(memberUpdateDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

    /**
     * Test updateMember with new email
     */
    @DisplayName("Test updateMember : new email")
    @Test
    void testUpdateMemberWithNewEmail(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setEmail("johnnyboy@mail.com");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.editMember(memberUpdateDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

    /**
     * Test updateMember with new password
     */
    @DisplayName("Test updateMember : new password")
    @Test
    void testUpdateMemberWithNewPassword(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setPassword("azertyuiop");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.editMember(memberUpdateDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

    /**
     * Test updateMember with new avatar
     */
    @DisplayName("Test updateMember : new avatar")
    @Test
    void testUpdateMemberWithNewAvatar(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setAvatar("myNewAvatarUrl");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.editMember(memberUpdateDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

    /**
     * Test updateMember with new phoneNumber
     */
    @DisplayName("Test updateMember : new phoneNumber")
    @Test
    void testUpdateMemberWithNewPhoneNumber(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setPhoneNumber("0123456789");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.editMember(memberUpdateDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

    /**
     * Test updateMember with null firstName throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null firstName throws IllegalArgumentException")
    @Test
    void testUpdateMemberWithNullFirstName() {
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setFirstName(null);
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.memberService.editMember(memberUpdateDTO, id));
    }

    /**
     * Test updateMember with null lastName throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null lastName throws IllegalArgumentException")
    @Test
    void testUpdateMemberWithNullLastName() {
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setLastName(null);
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.memberService.editMember(memberUpdateDTO, id));
    }

    /**
     * Test updateMember with null email throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null email throws IllegalArgumentException")
    @Test
    void testUpdateMemberWithNullEmail() {
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setEmail(null);
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.memberService.editMember(memberUpdateDTO, id));
    }

    /**
     * Test updateMember with null firstName throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null password throws IllegalArgumentException")
    @Test
    void testUpdateMemberWithNullPassword() {
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setPassword(null);
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.memberService.editMember(memberUpdateDTO, id));
    }

    //Method deleteMember

    /**
     * Test deleteMember successfully
     */
    @DisplayName("Test deleteMember successfully")
    @Test
    void testDeleteMemberSuccessful(){
        Role role = new Role(3L, "customer");
        Long id = 1L;
        Member member = createValidMember();
        BDDMockito.when(this.memberRepository.findMemberById(member.getId())).thenReturn(Optional.of(member));

        Member newMember = member;
        newMember.setFirstName("deletedMemberFirstName");
        newMember.setLastName("deletedMemberLastName");
        newMember.setPhoneNumber(null);
        newMember.setEmail("deletedMember@mail.com");
        newMember.setAvatar("default");
        newMember.setIsActive(false);
        MemberDeleteDTO memberDeleteDTO = MemberMappers.memberToDeleteDto(newMember);
        BDDMockito.when(this.memberRepository.save(ArgumentMatchers.any(Member.class))).thenReturn(newMember);

        Member result = null;
        try {
            result = this.memberService.deleteMember(memberDeleteDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newMember, result);
        }
    }

}
