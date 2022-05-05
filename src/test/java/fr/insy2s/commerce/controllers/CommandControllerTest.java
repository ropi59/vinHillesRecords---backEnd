package fr.insy2s.commerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.mappers.CommandMapper;
import fr.insy2s.commerce.mappers.MemberMappers;
import fr.insy2s.commerce.models.*;
import fr.insy2s.commerce.services.AdressService;
import fr.insy2s.commerce.services.CommandService;
import fr.insy2s.commerce.services.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest({CommandController.class})
@AutoConfigureMockMvc
class CommandControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandService commandService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private AdressService adressService;

    public Command createValidCommand(){
        Instant today = Instant.now();
        long ts = today.toEpochMilli();
        Instant todayIso = Instant.ofEpochMilli(ts);

        Instant livraison = Instant.now().plus(30, ChronoUnit.DAYS);
        long livr = livraison.toEpochMilli();
        Instant livraisonIso = Instant.ofEpochMilli(livr);

        Role role = new Role(3L, "customer");
        Status status = new Status(1L,"Livrée");
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("Dominique");
        member.setLastName("De Villepin");
        member.setEmail("lastRealPremier@Elysee.fr");
        member.setPhoneNumber("06789809898");
        member.setRole(role);
        member.setPassword("AZERTY");

        Adress adress = new Adress();
        adress.setId(1L);
        adress.setCountry("Belgium");
        adress.setStreetNumber("33");
        adress.setStreetName("Bakkerstraat");
        adress.setZipCode("8700");
        adress.setCity("Menen");

        Command command = Command.builder()
                .id(1L)
                .commandDate(todayIso)
                .deliveryDate(livraisonIso)
                .member(member)
                .status(status)
                .adress(adress)
                .build();
        return command;
    }
    public Member creatValidMember(){
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("Customer");

        Member member = new Member();
        member.setId(1L);
        member.setFirstName("Dominique");
        member.setLastName("De Villepin");
        member.setEmail("lastRealPremier@Elysee.fr");
        member.setPhoneNumber("06789809898");
        member.setRole(role);
        member.setPassword("AZERTY");
        return member;


    }
    public Adress createValidAdress(){
        Adress adress = new Adress();
        adress.setId(1L);
        adress.setCountry("Belgium");
        adress.setStreetNumber("33");
        adress.setStreetName("Bakkerstraat");
        adress.setZipCode("8700");
        adress.setCity("Menen");
        return adress;
    }

    @Test
    void createCommandValid() throws Exception {
        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        Command command = createValidCommand();
        CommandDto commandDto = CommandMapper.toDto(command);
    when(commandService.createCommand(commandDto)).thenReturn(commandDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/command/saveCommand")
                                .content(jsonMapper.writeValueAsString(commandDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void createCommandNonValid() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/command/saveCommand")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCommandValid() throws Exception {
        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        Command command = createValidCommand();
        CommandDto commandDto = CommandMapper.toDto(command);
        when(commandService.updateCommand(commandDto)).thenReturn(command);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/command/updateCommand")
                                .content(jsonMapper.writeValueAsString(commandDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Customer update completed"));
        ;
    }

    @Test
    void getAllCommands() throws Exception {
        Command command = createValidCommand();
        CommandDto commandDto = CommandMapper.toDto(command);

        Member member = creatValidMember();
        Adress adress = createValidAdress();
        Command commandToUpdate = CommandMapper.toEntity(commandDto,member,adress);
        List<Command> commands = new ArrayList<Command>();
        commands.add(commandToUpdate);
        when(commandService.getAllCommands()).thenReturn(commands);
        System.out.println(commands);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/command/getAllCommand"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    void deleteCommand() throws Exception {
        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        Command command = createValidCommand();
        CommandDto commandDto = CommandMapper.toDto(command);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/command/deleteCommand")
                                .content(jsonMapper.writeValueAsString(commandDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Customer got deleted"));
    }

    @Test
    void getCountCommand() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/command//getCountOfCommands"))
                .andExpect(status().isOk());
    }

    @Test
    void getCommandByMemberAndStatus() throws Exception {
        ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        Command command = createValidCommand();
        CommandDto commandDto = CommandMapper.toDto(command);        Member member = creatValidMember();
        Adress adress = createValidAdress();
        Status status = new Status(1L,"En livraison");
        List<CommandDto> commands = new ArrayList<>();
        commands.add(commandDto);
        MemberDTO memberDTO = MemberMappers.memberToMemberDto(member);
        when(commandService.findByMemberAndStatus(status,memberDTO)).thenReturn(commands);
        System.out.println("+++++++++"+commands);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/command/getCommandByMemberAndStatus")
                                .content(jsonMapper.writeValueAsString(status))
                                .content(jsonMapper.writeValueAsString(member))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}