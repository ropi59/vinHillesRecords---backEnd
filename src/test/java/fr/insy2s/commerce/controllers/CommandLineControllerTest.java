package fr.insy2s.commerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import fr.insy2s.commerce.mappers.CommandLineMapper;
import fr.insy2s.commerce.dtos.commandline.CommandLineDTO;
import fr.insy2s.commerce.mappers.CommandMapper;
import fr.insy2s.commerce.models.*;
import fr.insy2s.commerce.services.CommandLineService;
import fr.insy2s.commerce.services.CommandService;
import fr.insy2s.commerce.services.ProductService;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = CommandLineController.class)
class CommandLineControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandLineService commandLineService;
    @MockBean
    private ProductService productService;
    @MockBean
    private CommandService commandService;
    private final String route = "/commandlines";

    //Convenient methods

    Instant today = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    List<Tag> tagList = new ArrayList<>();
    List<Adress> adressList = new ArrayList<>();

    private CommandLine createValidCommandLine(){
        Role role = new Role(3L, "customer");
        Member member = new Member(1L, "john", "doe", "johndoe@mail.com", "010203040506", "azerty", true, "resetKey", "avatarurl", role, adressList);
        Status status = new Status(1L, "delivery");
        Product product = new Product(1L, "title", "artist", "description", 19.99F, "label", "format", 10, "coverUrl", today, today, tagList);
        Adress adress = new Adress(1L, "1", "main street", "95000", "Paris", "France");
        Command command = new Command(1L, today, status, today, member, adress);
        CommandLine commandLine = new CommandLine(1L, 19.99F, 1, product, command);
        return commandLine;
    }

    //Method findById

    /**
     * Test findCommandLineById when id is present in database
     */
    @DisplayName("Test findCommandLineById : id found")
    @Test
    void testFindCommandLineByIdFound()throws Exception{
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Mockito.when(this.commandLineService.findCommandLineById(id)).thenReturn(commandLineDTO);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Gson gson = new Gson();
        CommandLineDTO resultDDTO = gson.fromJson(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                CommandLineDTO.class);
        Assertions.assertNotNull(resultDDTO);
        assertThat(resultDDTO).usingRecursiveComparison().isEqualTo(commandLineDTO);
    }

    /**
     * Test findCommandLineById when id is not present in database
     */
    @DisplayName("Test findCommandLineById : id not found")
    @Test
    void testFindCommandLineByIdNotFound() throws Exception {
        Long id = 1L;
        Mockito.when(this.commandLineService.findCommandLineById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findCommandLineById when id is not valid
     */
    @DisplayName("Test findCommandLineById : invalid id")
    @Test
    void testFindCommandLineByInvalidId() throws Exception {
        Long id = 2992337203685477580L;
        Mockito.when(this.commandLineService.findCommandLineById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findCommandLineById when id is null
     */
    @DisplayName("Test findCommandLineById : null id")
    @Test
    void testFindCommandLineByNullId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test findCommandLineById when database is unreachable
     */
    @DisplayName("TestFindCommandLineById : database unreachable")
    @Test
    void testFindCommandLineByIdDatabaseUnreachable() throws Exception {
        Long id = 1L;
        Mockito.when(this.commandLineService.findCommandLineById(id)).thenThrow(RuntimeException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method createCommandLine

    /**
     * Test createCommandLine when the creation is successful
     */
    @DisplayName("test createCommandLine : creation successful")
    @Test
    void testCreateCommandLineSuccessful() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        CommandLineDTO commandLineDTO = body;
        Mockito.when(this.commandLineService.createCommandLine(body)).thenReturn(commandLineDTO);
        Gson gson = new Gson();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gson.toJson(body)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        CommandLineDTO resultDTO = gson.fromJson(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                CommandLineDTO.class);
        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(commandLineDTO, resultDTO);
    }

    /**
     * Test createCommandLine with null body
     */
    @DisplayName("Test createCommandLine : null body")
    @Test
    void testCreateCommandLineNullBody() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(route))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine with 0 unitePrice
     */
    @DisplayName("Test createCommandLine : null unitePrice")
    @Test
    void testCreateCommandLine0UnitePrice() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        body.setUnitePrice(0);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gson.toJson(body)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine with 0 quantity
     */
    @DisplayName("Test createCommandLine : null quantity")
    @Test
    void testCreateCommandLine0Quantity() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        body.setQuantity(0);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine with null productId
     */
    @DisplayName("Test createCommandLine : null productId")
    @Test
    void testCreateCommandLine0ProductId() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        body.setProductId(null);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine with null commandId
     */
    @DisplayName("Test createCommandLine : null commandId")
    @Test
    void testCreateCommandLine0CommandId() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        body.setCommandId(null);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine with negative unitePrice
     */
    @DisplayName("Test createCommandLine : negative unitePrice")
    @Test
    void testCreateCommandLineNegativeUnitePrice() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        body.setUnitePrice(-10);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine with negative quantity
     */
    @DisplayName("Test createCommandLine : negative quantity")
    @Test
    void testCreateCommandLineNegativeQuantity() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        body.setQuantity(-10);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine when creation failed
     */
    @DisplayName("Test createCommandLine : creation failed")
    @Test
    void testCreateCommandLineFailed() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Mockito.when(this.commandLineService.createCommandLine(body)).thenReturn(null);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test createCommandLine throws Exception
     */
    @DisplayName("Test createCommandLine : throws Exception")
    @Test
    void testCreateCommandLineThrowsException() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO body = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Mockito.when(this.commandLineService.createCommandLine(body)).thenThrow(NullPointerException.class);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gson.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method updateCommandLine

    /**
     * Test update commandLine successfully
     */
    @DisplayName("Test updateCommandLine : update successful************************")
    @Test
    void testUpdateCommandLineSuccessfully() throws Exception {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        CommandLine newCommandLine = createValidCommandLine();
        newCommandLine.setQuantity(5);
        CommandLineDTO commandLineToUpdateDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Mockito.when(this.commandLineService.updateCommandLine(commandLineToUpdateDTO, id)).thenReturn(newCommandLine);
        Mockito.when(this.productService.findOneProductById(1L)).thenReturn(commandLine.getProduct());
        Mockito.when(this.commandService.findOneById(1L)).thenReturn(CommandMapper.toDto(commandLine.getCommand()));

        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(route+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(jsonMapper.writeValueAsString(commandLineToUpdateDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CommandLine response = jsonMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), CommandLine.class);
        assertThat(newCommandLine).usingRecursiveComparison().isEqualTo(response);
        assertThat(commandLine).usingRecursiveComparison().isNotEqualTo(response);
    }

    /**
     * Test updateCommandLine with null body
     */
    @DisplayName("Test updateCommandLine : null body")
    @Test
    void testUpdateCommandLineNullBody() throws Exception {
        Long id = 1L;
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                .put(route+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gson.toJson(null)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test updateCommandLine when update method throw exception
     */
    @DisplayName("Test updateCommandLine : throws Exception")
    @Test
    void testCreateCommandLineCatchException() throws Exception {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineUpdateDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Mockito.when(this.commandLineService.updateCommandLine(commandLineUpdateDTO, id)).thenThrow(new RuntimeException());
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                .put(route+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gson.toJson(null)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test updateCommandLine with invalid body
     */
    @DisplayName("Test updateCommandLine : invalid field(s)")
    @Test
    void updateCommandLineInvalidFiel() throws Exception {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        commandLine.setQuantity(-10);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                .put(route+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gson.toJson(commandLineDTO)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Method deleteCommandLine

    /**
     * Test deleteCommandLine successfully
     */
    @DisplayName("Test deleteCommandLine : successful")
    @Test
    void testDeleteCommandLineSuccessfully() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete(route)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gson.toJson(commandLineDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
            .andExpect(MockMvcResultMatchers.jsonPath("$").value("true"));
    }

    /**
     * Test deleteCommandLine when commandLine not exist
     */
    @DisplayName("Test deleteCommandLine : commandLine not exist")
    @Test
    void testDeleteCommandLineIdNotValid() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setId(0L);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(commandLineDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
            .andExpect(MockMvcResultMatchers.jsonPath("$").value("true"));
    }

    /**
     * Test deleteCommandLine when id is null
     */
    @DisplayName("Test deleteCommandLine : id null")
    @Test
    void testDeleteCommandLineNullId() throws Exception {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setId(null);
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(route)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(commandLineDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("true"));
    }

}
