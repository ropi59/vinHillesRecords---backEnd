package fr.insy2s.commerce.controllers;

import com.google.gson.Gson;
import fr.insy2s.commerce.dtos.adress.AdressDTONoId;
import fr.insy2s.commerce.mappers.AdressMapper;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.services.AdressService;
import fr.insy2s.commerce.services.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebMvcTest(controllers = AdressController.class)
class AdressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private AdressService adressService;
    private final String route = "/adress";

    private Member createMember() {
        Role role = new Role(1L, "admin");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("wil");
        member.setLastName("case");
        member.setEmail("wil@gmail.com");
        member.setPassword("12345");
        member.setRole(role);
        return member;
    }

    private Adress createValidAdress() {
        Member member = createMember();
        Adress adress = new Adress();
        adress.setId(1L);
        adress.setStreetNumber("1");
        adress.setCity("lille");
        adress.setCountry("France");
        adress.setStreetName("leon blum");
        adress.setZipCode("59000");
        List<Member> members = new ArrayList<>();
        members.add(member);
        adress.setMemberList(members);
        return adress;
    }
    private Adress createDeleteAdress() {
        Member member = createMember();
        Adress adress = new Adress();
        adress.setId(1L);
        adress.setStreetNumber("1");
        adress.setCity("delete city adress");
        adress.setCountry("delete country adress");
        adress.setStreetName("delete street adress");
        adress.setZipCode("delete zipcode adress");
        List<Member> members = new ArrayList<>();
        members.add(member);
        adress.setMemberList(members);
        return adress;
    }

    // Method findById

    /**
     * Test findAdressById when id is present in database
     */
    @DisplayName("Test findAdressById : id found")
    @Test
    void testFindAdressByIdFound() throws Exception {
        Long id = 1L;
        AdressDTONoId adressDTO = AdressMapper.toAdressDto(createValidAdress());
        Mockito.when(this.adressService.findAdressById(id)).thenReturn(adressDTO);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Gson gson = new Gson();
        AdressDTONoId resultDTO = gson.fromJson(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                AdressDTONoId.class);
        Assertions.assertNotNull(resultDTO);
        assertThat(resultDTO).usingRecursiveComparison().isEqualTo(adressDTO);
    }
}