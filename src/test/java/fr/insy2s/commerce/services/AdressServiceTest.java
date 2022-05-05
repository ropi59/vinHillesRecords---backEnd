package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.AdressMapper;
import fr.insy2s.commerce.dtos.adress.AdressDTONoId;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.repositories.AdressRepository;
import fr.insy2s.commerce.repositories.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdressServiceTest {

    @MockBean // simulation of the repo (create mock)
    private AdressRepository adressRepository;


    @Autowired
    private AdressService adressService;

    @MockBean
    private MemberRepository memberRepository;



   /* @BeforeAll // before start the test, initiate service
    void init() {
        adressService = new AdressServiceImpl(adressRepository,adressMapper);
    }*/


    private Member createMember() {
        Role role = new Role(1L, "admin");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("wil");
        member.setLastName("case");
        member.setEmail("wil@gmail.com");
        member.setPassword("12345");
        member.setRole(role);
        member.setAdressList(new ArrayList<Adress>());
        return member;
    }

    private Adress createAdress() {
        Member member = createMember();
        member.setId(2L);
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

    private List<Long> createMemberIdList() {
        List<Long> memberIdList = new ArrayList<>();
        memberIdList.add(1L);
        return memberIdList;
    }

    private List<Member> createMembers() {
        List<Member> members = new ArrayList<>();

        return members;
    }

    /**
     * Test findAdressById when adress is found
     */
    @DisplayName("Test findAdressById : adress found")
    @Test
    void testFindAdressById() {
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
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        AdressDTONoId result = this.adressService.findAdressById(1L);
        Assertions.assertNotNull(result);
        AdressDTONoId adressToCompare = AdressMapper.toAdressDto(adress);
        Assertions.assertEquals(adressToCompare, result);
    }

    /**
     * Test findAdressById when adress is not found
     */
    @DisplayName("Test findAdressById : adress not found")
    @Test
    void testFindAdressByIdAdressNotFound() {
        Long id = 123456789L;
        BDDMockito.when(this.adressRepository.findAdressById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(this.adressService.findAdressById(id));
    }

    /**
     * Test findAdressById when adress is invalid
     */
    @DisplayName("Test findAdressById : invalid id")
    @Test
    void testFindAdressByInvalidId() throws Exception {
        Long id = 122333546847979794L;
        Assertions.assertNull(this.adressService.findAdressById(id));
    }

    /**
     * Test createAdress when is successful
     */
    @DisplayName("test createAdress : creation successful")
    @Test
    @Disabled
    void testCreateAdressSuccessful() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "sdf", "rue sdf", "59000", "lille", "france", createMemberIdList());
        Adress adress = AdressMapper.toAdress(adressDTONoId);
        BDDMockito.when(adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(adress);
        AdressDTONoId result = this.adressService.save(adressDTONoId);
        adressDTONoId = AdressMapper.toAdressDto(adress);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(adressDTONoId, result);
    }

    /**
     * Test findAdressById when adress is null
     */
    @DisplayName("Test findAdressById : id  is null")
    @Test
    void testFindAdressIdNullId() throws Exception {
        Long id = null;
        Assertions.assertNull(this.adressService.findAdressById(id));
    }

    /**
     * Test createAdress when adressDTO is null
     */
    @DisplayName("Test createAdress : adress is null")
    @Test
    @Disabled
    void testCreateAdressNullDTO() {
        AdressDTONoId adressDTONoId = Assertions.assertDoesNotThrow(() -> this.adressService.save(null));
        Assertions.assertNull(adressDTONoId);
    }

    /**
     * Test createAdress when number is null
     */
    @DisplayName("Test createAdress: number is null")
    @Test
    void testCreateAdressNumberNull() {
        AdressDTONoId adressDtoNoIdToSave = new AdressDTONoId( null, "rue sdf", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressRepository.save(AdressMapper.toAdress(adressDtoNoIdToSave)));
    }

    /**
     * Test createAdress when street is null
     */
    @DisplayName("Test createAdress: street is null ")
    @Test
    void testCreateAdressStreetNull() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "1234", null, "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when zipCode is null
     */
    @DisplayName("Test createAdress: zipCode is null ")
    @Test
    void testCreateAdressZipCodeNull() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "4567", "rue sdf", null, "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when city is null
     */
    @DisplayName("Test createAdress: city is null ")
    @Test
    void testCreateAdressCityNull() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "42514", "rue sdf", "59000", null, "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when country is null
     */
    @DisplayName("Test createAdress: country is null ")
    @Test
    void testCreationAdressCountryNull() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "48593", "rue sdf", "59000", "lille", null, createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when list of member is null
     */
    @DisplayName("Test createAdress: list of member is null ")
    @Test
    void testCreationAdressListOfMemberEmpty() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "4135252", "rue sdf", "59000", "lille", "france", null);
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    //empty

    /**
     * Test createAdress when number is empty
     */
    @DisplayName("Test createAdress: number is empty")
    @Test
    void testCreateAdressNumberEmpty() {
        AdressDTONoId adressDtoNoIdToSave = new AdressDTONoId( "", "rue sdf", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDtoNoIdToSave));
    }

    /**
     * Test createAdress when street is empty
     */
    @DisplayName("Test createAdress: street is empty ")
    @Test
    void testCreateAdressStreetEmpty() {
        AdressDTONoId adressDTONoId = new AdressDTONoId( "1234", "", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when zipCode is empty
     */
    @DisplayName("Test createAdress: zipCode is empty ")
    @Test
    void testCreateAdressZipCodeEmpty() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("4567", "rue sdf", "", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when city is empty
     */
    @DisplayName("Test createAdress: city is empty ")
    @Test
    void testCreateAdressCityEmpty() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("42514", "rue sdf", "59000", "", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    /**
     * Test createAdress when country is empty
     */
    @DisplayName("Test createAdress: country is empty ")
    @Test
    void testCreationAdressCountryEmpty() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("48593", "rue sdf", "59000", "lille", "", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
    }

    //not valid
//******************************************************************************************************************
    @DisplayName("Test createAdress: number not valid ")
    @Test
    void testCreateAdressInvalidNumber() {
        Member member = createMember();
        BDDMockito.when(this.memberRepository.save(member)).thenReturn(member);
        BDDMockito.when(this.memberRepository.findMemberById(1L)).thenReturn(Optional.of(member));
        AdressDTONoId adressDtoNoIdToSave = new AdressDTONoId( "qsdfazer", "rue sdf", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDtoNoIdToSave));
        AdressDTONoId adressDtoNoIdToSave1 = new AdressDTONoId("123qsdfe", "rue sdf", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDtoNoIdToSave1));
        AdressDTONoId adressDtoNoIdToSave2 = new AdressDTONoId( "2554644444444444444444", "rue sdf", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDtoNoIdToSave2));
        AdressDTONoId adressDtoNoIdToSave3 = new AdressDTONoId("°09°àçç_", "1", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDtoNoIdToSave3));
        AdressDTONoId adressDtoNoIdToSave4 = new AdressDTONoId( "123 123 123", "1", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDtoNoIdToSave4));
    }

    /**
     * Test createAdress when street is not valid
     */
    @DisplayName("Test createAdress: street is not valid ")
    @Test
    @Disabled
    void testCreateAdressInvalidStreet() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("1234", "qsdfqsdfqs", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
        AdressDTONoId adressDTONoId1 = new AdressDTONoId("1234", "rue blum456", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId1));
        AdressDTONoId adressDTONoId2 = new AdressDTONoId( "1234", "RUE BLUM", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId2));
        AdressDTONoId adressDTONoId3 = new AdressDTONoId("1234", "rue blummmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId3));
        AdressDTONoId adressDTONoId4 = new AdressDTONoId("1234", "/@^Lille", "59000", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId4));
    }

    /**
     * Test createAdress when zipCode is not valid
     */
    @DisplayName("Test createAdress: zipCode is not valid ")
    @Test
    @Disabled
    void testCreateAdressInvalidZipCode() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("4567", "rue sdf", "lihoiu", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
        AdressDTONoId adressDTONoId1 = new AdressDTONoId("4567", "rue sdf", "", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId1));
        AdressDTONoId adressDTONoId2 = new AdressDTONoId("4567", "rue sdf", "", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId2));
        AdressDTONoId adressDTONoId3 = new AdressDTONoId( "4567", "rue sdf", "", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId3));
        AdressDTONoId adressDTONoId4 = new AdressDTONoId( "4567", "rue sdf", "", "lille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId4));
    }

    /**
     * Test createAdress when city is not valid
     */
    @DisplayName("Test createAdress: city is not valid ")
    @Test
    @Disabled
    void testCreateAdressInvalidCity() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("4567", "rue sdf", "59000", "lilleeeeeeeeeeeeeeeeeeeeeeeee", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
        AdressDTONoId adressDTONoId1 = new AdressDTONoId("4567", "rue sdf", "59000", "LILLE", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId1));
        AdressDTONoId adressDTONoId2 = new AdressDTONoId("4567", "rue sdf", "59000", "L9090ille", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId2));
        AdressDTONoId adressDTONoId3 = new AdressDTONoId("4567", "rue sdf", "59000", "   lille    ", "france", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId3));
    }

    /**
     * Test createAdress when country is not valid
     */
    @DisplayName("Test createAdress: country is not valid ")
    @Test
    void testCreationAdressInvalidCountry() {
        AdressDTONoId adressDTONoId = new AdressDTONoId("4567", "rue sdf", "", "lille", "france1234", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId));
        AdressDTONoId adressDTONoId1 = new AdressDTONoId("4567", "rue sdf", "", "lille", "   france  ", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId1));
        AdressDTONoId adressDTONoId2 = new AdressDTONoId("4567", "rue sdf", "", "lille", "franceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId2));
        AdressDTONoId adressDTONoId3 = new AdressDTONoId("4567", "rue sdf", "", "lille", "FrAnCe", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId3));
        AdressDTONoId adressDTONoId4 = new AdressDTONoId("4567", "rue sdf", "", "lille", "franc", createMemberIdList());
        Assertions.assertNull(this.adressService.save(adressDTONoId4));
    }

    /**
     * Test updateAdress with new number
     */
    @DisplayName("Test updateAdress : new number")
    @Test
    void testUpdateAdressNewNumber() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setStreetNumber("14");
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);

        Adress result = null;
        try {
            result = this.adressService.update(adressUpdateDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newAdress, result);
        }
    }

    /**
     * Test updateAdress with new street
     */
    @DisplayName("Test updateStreet : new street")
    @Test
    void testUpdateAdressNewStreet() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setStreetName("15");
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);

        Adress result = null;
        try {
            result = this.adressService.update(adressUpdateDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newAdress, result);
        }
    }

    /**
     * Test updateAdress with new zipCode
     */
    @DisplayName("Test updateZipCode : new zipCode")
    @Test
    void testUpdateAdressNewZipCode() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setZipCode("62000");
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);

        Adress result = null;
        try {
            result = this.adressService.update(adressUpdateDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newAdress, result);
        }
    }

    /**
     * Test updateAdress with new city
     */
    @DisplayName("Test updateCity : new city")
    @Test
    void testUpdateAdressNewCity() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setCity("milan");
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);

        Adress result = null;
        try {
            result = this.adressService.update(adressUpdateDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newAdress, result);
        }
    }

    /**
     * Test updateAdress with new country
     */
    @DisplayName("Test updateCountry : new country")
    @Test
    void testUpdateAdressNewCountry() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setCountry("rome");
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);

        Adress result = null;
        try {
            result = this.adressService.update(adressUpdateDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newAdress, result);
        }
    }

    /**
     * Test updateAdress with null number throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null number throws IllegalArgumentException")
    @Test
    void testUpdateAdressWithNullNumber(){
        Adress adress = createAdress() ;
        Long id= 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setStreetNumber(null);
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.adressService.update(adressUpdateDTO, id));
    }

    /**
     * Test updateAdress with null street throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null street throws IllegalArgumentException")
    @Test
    void testUpdateAdressWithNullStreet() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setStreetName(null);
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.adressService.update(adressUpdateDTO, id));
    }

    /**
     * Test updateAdress with null zipCode throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null zipCode throws IllegalArgumentException")
    @Test
    void testUpdateAdressWithNullZipCode() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setZipCode(null);
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.adressService.update(adressUpdateDTO, id));
    }

    /**
     * Test updateAdress with null city throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null city throws IllegalArgumentException")
    @Test
    void testUpdateAdressWithNullCity() {
        Adress adress = createAdress();
        Long id = 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setCity(null);
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.adressService.update(adressUpdateDTO, id));
    }

    /**
     * Test updateAdress with null country throw IllegalArgumentException
     */
    @DisplayName("Test updateMember with null country throws IllegalArgumentException")
    @Test
    void testUpdateAdressWithNullCountry(){
        Adress adress = createAdress() ;
        Long id= 1L;
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));
        Adress newAdress = adress;
        newAdress.setCountry(null);
        AdressDTONoId adressUpdateDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.adressService.update(adressUpdateDTO, id));
    }

    /**
     * Test deleteAdress successfully
     *//*
    @DisplayName("Test deleteAdress successfully")
    @Test
    void testDeleteAdressSuccessful(){
        Long id = 1L;
        Adress adress = createAdress();
        BDDMockito.when(this.adressRepository.findAdressById(adress.getId())).thenReturn(Optional.of(adress));

        Adress newAdress = adress;
        newAdress.setNumber("deletedAdressNumber");
        newAdress.setZipCode("deletedAdressZipCode");
        newAdress.setCountry("deleteAdressCountry");
        newAdress.setCity("deletedAdressCity");
        newAdress.setMemberList(null);

        AdressDTO adressDTO = AdressMapper.toAdressDto(newAdress);
        BDDMockito.when(this.adressRepository.save(ArgumentMatchers.any(Adress.class))).thenReturn(newAdress);

        Adress result = null;
        try {
            result = this.adressService.delete(adressDTO,id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newAdress, result);
        }
    }*/
}