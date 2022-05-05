package fr.insy2s.commerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.insy2s.commerce.mappers.RefundMapper;
import fr.insy2s.commerce.dtos.refund.RefundDTO;
import fr.insy2s.commerce.models.*;
import fr.insy2s.commerce.services.RefundService;
import org.junit.jupiter.api.Assertions;
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

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(controllers = RefundController.class)
class RefundControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RefundService refundService;
    private final String route = "/refunds";

    //Convenient method

    Date date = new Date();
    Instant today = Instant.now();
    List<Tag> tagList = new ArrayList<>();
    List<Adress> adressList = new ArrayList<>();

    private Refund createValidRefund(){
        Role role = new Role(3L, "customer");
        Member member = new Member(1L, "john", "doe", "johndoe@mail.com", "010203040506", "azerty", true, "resetKey", "avatarurl", role, adressList);
        Status status = new Status(1L, "delivery");
        Product product = new Product(1L, "title", "artist", "description", 19.99F, "label", "format", 10, "coverUrl", today, today, tagList);
        Adress adress = new Adress(1L, "1", "main street", "95000", "Paris", "France");
        Command command = new Command(1L, today, status, today, member, adress);
        CommandLine commandLine = new CommandLine(1L, 19.99F, 1, product, command);
        Refund refund = new Refund(1L, "accepted", today, "reason i'm asking for refund", commandLine);
        return refund;
    }

    //Method findById

    /**
     * Test findRefundById when id is in database
     */
    @DisplayName("Test findRefundById : id found")
    @Test
    void testFindRefundByIdFound() throws Exception {
        Long id = 1L;
        Refund refund = createValidRefund();
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        Mockito.when(this.refundService.findRefundById(id)).thenReturn(refundDTO);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        RefundDTO resultDTO = jsonMapper.readValue(
                result.getResponse().getContentAsString(), RefundDTO.class);
        Assertions.assertNotNull(resultDTO);
        assertThat(resultDTO).usingRecursiveComparison().isEqualTo(refundDTO);
    }

    /**
     * Test findRefundById when id is not present in database
     */
    @DisplayName("Test findRefundById : id not found")
    @Test
    void testFindRefundByIdNotFound() throws Exception {
        Long id = 1L;
        Mockito.when(this.refundService.findRefundById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findRefundById when id is null
     */
    @DisplayName("Test findRefundById : null id")
    @Test
    void testFindRefundByNullId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test findRefundByID when id is invalid
     */
    @DisplayName("Test findRefundById : invalid id")
    @Test
    void testFindRefundByInvalidId() throws Exception {
        Long id = 2992337203685477580L;
        Mockito.when(this.refundService.findRefundById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findRefundByID when database is unreachable
     */
    @DisplayName("Test findRefundById : database unreachable")
    @Test
    void testFindRefundByIdDatabaseUnreachable () throws Exception {
        Long id = 1L;
        Mockito.when(this.refundService.findRefundById(id)).thenThrow(RuntimeException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method createRefund

    /**
     * Test createRefund when the creation is successful
     */
    @DisplayName("Test createRefund : creation successful**************************************")
    @Test
    void testCreateRefundSuccessful() throws Exception {
        Refund refund = createValidRefund();
        RefundDTO body = RefundMapper.refundToRefundDTO(refund);
        RefundDTO refundDTO = body;
        Mockito.when(this.refundService.createRefund(body)).thenReturn(refundDTO);
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
        Object toto = result.getResponse().getContentAsString();
        RefundDTO resultDTO = jsonMapper.readValue(
                result.getResponse().getContentAsString(), RefundDTO.class);

        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(refundDTO, resultDTO);
    }
}
