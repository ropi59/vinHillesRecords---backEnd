package fr.insy2s.commerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import fr.insy2s.commerce.mappers.MemberMappers;
import fr.insy2s.commerce.dtos.member.MemberCreationDTO;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.dtos.member.MemberDeleteDTO;
import fr.insy2s.commerce.dtos.member.MemberUpdateDTO;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.services.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;

import java.nio.charset.StandardCharsets;


@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    private final String route = "/members";

    //Convenient Method for testing

    private Member createValidMember(String email){
        Role role = new Role(3L, "customer");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("john");
        member.setLastName("doe");
        member.setEmail(email);
        member.setPassword("azerty");
        member.setRole(role);
        return member;
    }

    private Member createDeletedMember(){
        Role role = new Role(3L, "customer");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("deletedMemberFirstName");
        member.setLastName("deletedMemberLastName");
        member.setEmail("deletedMember@mail.com");
        member.setPhoneNumber(null);
        member.setAvatar("default");
        member.setIsActive(false);
        member.setRole(role);
        return member;
    }

    // Method findById

    /**
     * Test findMemberById when id is present in database
     */
    @DisplayName("Test findMemberById : id found")
    @Test
    void testFindMemberByIdFound() throws Exception {
        Long id = 1L;
        MemberDTO dto = new MemberDTO("john", "doe", "johndoe@mail.com", 3L);
        Mockito.when(this.memberService.findMemberById(id)).thenReturn(dto);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Gson gson = new Gson();
        MemberDTO resultDTO = gson.fromJson(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                MemberDTO.class);
        Assertions.assertNotNull(resultDTO);
        assertThat(resultDTO).usingRecursiveComparison().isEqualTo(dto);
    }

    /**
     *  Test findMemberById when id is not present in Database
     */
    @DisplayName("Test findMemberById : id not found")
    @Test
    void testFindMemberByIdNotFound() throws Exception {
        Long id = 1L;
        Mockito.when(this.memberService.findMemberById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findMemberById when id is not valid
     */
    @DisplayName("Test findMemberById : invalid id")
    @Test
    void testFindMemberByIdInvalid() throws Exception {
        Long id = 2992337203685477580L;
        Mockito.when(this.memberService.findMemberById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findMemberById when id is null
     */
    @DisplayName("Test findMemberById : null id")
    @Test
    void testFindMemberByIdNull() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test findMemberById when database is unreachable
     */
    @DisplayName( "Test findMemberById : Database unreachable")
    @Test
    void testFindMemberByIdDatabaseUnreachable() throws Exception {
        Long id = 1L;
        Mockito.when(this.memberService.findMemberById(id)).thenThrow(RuntimeException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method createMember

    /**
     * Test createMember when the member creation on the service is successful
     */
    @DisplayName("Test CreateMember : creation successful")
    @Test
    void testCreateMemberSuccessful() throws Exception {
        Role role = new Role(3L, "customer");
        MemberCreationDTO body = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azertyuiop", role);
        MemberDTO memberDTO = new MemberDTO("john", "doe", "johndoe@mail.com", 3L);

        // TODO : check trainer why this doesnt work : Mockito.when(this.memberService.createMember(body)).thenReturn(memberDTO);
        Mockito.when(this.memberService.createMember(any(MemberCreationDTO.class))).thenReturn(memberDTO);

        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        MemberDTO resultDTO = jsonMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                MemberDTO.class);
        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(memberDTO, resultDTO);
    }

    /**
     * Test createMember with null body
     */
    @DisplayName("Test createMember : null body")
    @Test
    void testCreateMemberNullBody() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(route))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember with a null password
     */
    @DisplayName("Test createMember : null password")
    @Test
    void testCreateMemberNullPassword() throws Exception {
        Role role = new Role(3L, "customer");
        MemberCreationDTO body = new MemberCreationDTO("john", "doe", "johndoe@mail.com", null, role);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember with a null email
     */
    @DisplayName("Test createMember : null email")
    @Test
    void testCreateMemberNullEmail() throws Exception {
        Role role = new Role(3L, "customer");
        MemberCreationDTO body = new MemberCreationDTO("john", "doe", null, "azerty", role);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember with a null first_name
     */
    @DisplayName("Test createMember : null first_name")
    @Test
    void testCreateMemberNullFirstName() throws Exception {
        Role role = new Role(3L, "customer");
        MemberCreationDTO body = new MemberCreationDTO(null, "doe", "johndoe@mail.com", "azerty", role);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember with a null first_name
     */
    @DisplayName("Test createMember : null last_name")
    @Test
    void testCreateMemberNullLastName() throws Exception {
        Role role = new Role(3L, "customer");
        MemberCreationDTO body = new MemberCreationDTO("john", null, "johndoe@mail.com", "azerty", role);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember with a null role
     */
    @DisplayName("Test createMember : null role")
    @Test
    void testCreateMemberNullRole() throws Exception {
        MemberCreationDTO body = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azerty", null);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember when user creation on service failed
     */
    @DisplayName("Test createMember : user creation failed")
    @Test
    void testCreateMemberCreationFailed() throws Exception {
        MemberCreationDTO body = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azerty", null);
        Mockito.when(this.memberService.createMember(body)).thenReturn(null);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createMember when user creation on the service throw an exception
     */
    @DisplayName("Test createMember : throws exception")
    @Test
    void testCreateMemberThrowException() throws Exception {
        MemberCreationDTO body = new MemberCreationDTO("john", "doe", "johndoe@mail.com", "azerty", null);
        Mockito.when(this.memberService.createMember(body)).thenThrow(NullPointerException.class);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method update member

    /**
     * Test update member successfully
     */
    @DisplayName("Test updateMember : update successful")
    @Test
    void testUpdateMemberSuccessful() throws Exception {
        Long id = 1L;
        Member member = createValidMember("johndoe@mail.com");
        Member newMember = createValidMember("johndoe@mail.com");
        newMember.setEmail("johnnyboy@mail.com");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(member);
        Mockito.when(memberService.editMember(memberUpdateDTO, id)).thenReturn(newMember);

        Gson gson = new Gson();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(memberUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Member response = gson.fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8), Member.class);
        assertThat(newMember).usingRecursiveComparison().isEqualTo(response);
        assertThat(member).usingRecursiveComparison().isNotEqualTo(response);
    }

    /**
     * Test update member with null body
     */
    @DisplayName("Test updateMember : null body ")
    @Test
    void testUpdateMemberNullBody() throws Exception {
        long id = 1L;
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test member update when method update member throw exception
     */
    @DisplayName("Test updateMember : throws exception ")
    @Test
    void testUpdateMemberCatchException() throws Exception {
        Long id = 1L;
        Member member = createValidMember("johndoe@mail.com");
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(member);
        Mockito.when(this.memberService.editMember(memberUpdateDTO, id)).thenThrow(new RuntimeException());
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test update member with illegal argument, exception expected
     */
    @DisplayName("Test updateMember : invalid Field(s)")
    @Test
    void updateMemberWithInvalidField() throws Exception {
        long id = 1L;
        Member member = createValidMember( null);
        MemberUpdateDTO memberUpdateDTO = MemberMappers.memberToUpdateDto(member);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(memberUpdateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method delete member

    /**
     * Test delete member successfully
     */
    @DisplayName("Test deleteMember : delete successful")
    @Test
    void testDeleteMemberSuccessful() throws Exception {
        Long id = 1L;
        Member member = createValidMember("johndoe@mail.com");
        Member memberDeleted = createDeletedMember();
        MemberDeleteDTO memberDeleteDTO = MemberMappers.memberToDeleteDto(member);
        Mockito.when(memberService.deleteMember(memberDeleteDTO, id)).thenReturn(memberDeleted);

        Gson gson = new Gson();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(memberDeleteDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Member response = gson.fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8), Member.class);
        assertThat(memberDeleted).usingRecursiveComparison().isEqualTo(response);
        assertThat(member).usingRecursiveComparison().isNotEqualTo(response);
    }

    /**
     * Test delete member with null body
     */
    @DisplayName("Test deleteMember : null body ")
    @Test
    void testDeleteMemberNullBody() throws Exception {
        long id = 1L;
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test member delete when method update member throw exception
     */
    @DisplayName("Test deleteMember : throws exception ")
    @Test
    void testDeleteMemberCatchException() throws Exception {
        Long id = 1L;
        Member member = createValidMember("johndoe@mail.com");
        MemberDeleteDTO memberDeleteDTO = MemberMappers.memberToDeleteDto(member);
        Mockito.when(this.memberService.deleteMember(memberDeleteDTO, id)).thenThrow(new RuntimeException());
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test delete member with illegal argument, exception expected
     */
    @DisplayName("Test deleteMember : invalid Field(s)")
    @Test
    void deleteMemberWithInvalidField() throws Exception {
        long id = 1L;
        Member member = createValidMember( null);
        MemberDeleteDTO memberDeleteDTO = MemberMappers.memberToDeleteDto(member);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put(route+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(memberDeleteDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method findAll



}