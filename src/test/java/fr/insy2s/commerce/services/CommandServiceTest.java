package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.CommandMapper;
import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.exceptions.CommandRequestExceptions;
import fr.insy2s.commerce.models.*;
import fr.insy2s.commerce.repositories.AdressRepository;
import fr.insy2s.commerce.repositories.CommandRepository;
import fr.insy2s.commerce.repositories.MemberRepository;
import fr.insy2s.commerce.services.interfaces.CommandServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith does bring mockito annotation we don't have to instantiate them anymore
@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

    @Mock
    private CommandRepository commandRepositoryTest;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AdressRepository adressRepository;

    private CommandServiceInterface commandServiceInterface;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AdressService adressService;

    @BeforeEach
    void initUseCase() {
        commandServiceInterface = new CommandService(commandRepositoryTest,memberRepository,adressRepository,memberService,adressService);
    }

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

    /**
     * We test the creation of a command with mocking(Simulation)
     */
    @Test
    void testCreateCommandWithNoProblems() {
        //Given
        Command command = createValidCommand();
        CommandDto commandDtoTest = CommandMapper.toDto(command);
        //When
        when(adressRepository.findAdressById(command.getAdress().getId())).thenReturn(Optional.ofNullable(command.getAdress()));
        when(memberRepository.findMemberById(command.getMember().getId())).thenReturn(Optional.ofNullable(command.getMember()));
        when(commandRepositoryTest.save(ArgumentMatchers.any(Command.class))).thenReturn(command);

        CommandDto savedCommandDto = commandServiceInterface.createCommand(commandDtoTest);
        //then
        assertNotNull(savedCommandDto);
        assertEquals(commandDtoTest,savedCommandDto);
    }

    @Test
    void testCreateCommandWithDeliveryDateBehind() {
        //Given
        Instant today = Instant.now();
        long ts = today.toEpochMilli();
        Instant todayIso = Instant.ofEpochMilli(ts);

        Instant livraison = Instant.now().minus(30, ChronoUnit.DAYS);
        long livr = livraison.toEpochMilli();
        Instant livraisonIso = Instant.ofEpochMilli(livr);

        CommandDto commandDto = CommandDto.builder()
                .commandDate(todayIso)
                .deliveryDate(livraisonIso)
                .memberId(1L)
                .status(1L)
                .adressId(1L)
                .build();
        //When
        assertThrows(IllegalArgumentException.class, () -> commandServiceInterface.createCommand(commandDto));
    }

    @Test
    void testCreateCommandWithoutArgs() {
        //Given
        Command command = createValidCommand();
        CommandDto commandDtoTest = CommandMapper.toDto(command);
        //then
        assertThrows(IllegalArgumentException.class, () -> commandServiceInterface.createCommand(commandDtoTest));
    }

    @Test
    void testCreateCommandWithMemberNull() {
        //Given
        Role role = new Role(1L,"Admin");
        Status status = new Status(1L,"Arrivé");

        Instant today = Instant.now();
        long ts = today.toEpochMilli();
        Instant todayIso = Instant.ofEpochMilli(ts);

        Instant livraison = Instant.now().minus(30, ChronoUnit.DAYS);
        long livr = livraison.toEpochMilli();
        Instant livraisonIso = Instant.ofEpochMilli(livr);

        CommandDto commandDto = CommandDto.builder()
                .commandDate(todayIso)
                .deliveryDate(livraisonIso)
                .memberId(null)
                .status(1L)
                .adressId(1L)
                .build();

        Member member = creatValidMember();
        Adress adress = createValidAdress();
        //When
        assertThrows(IllegalArgumentException.class, () -> commandServiceInterface.createCommand(commandDto));
    }

    @Test
    void testCreateCommandWithAdressNull() {
        //Given
        Role role = new Role(1L,"Admin");
        Status status = new Status(1L,"Arrivé");

        Instant today = Instant.now();
        long ts = today.toEpochMilli();
        Instant todayIso = Instant.ofEpochMilli(ts);

        Instant livraison = Instant.now().minus(30, ChronoUnit.DAYS);
        long livr = livraison.toEpochMilli();
        Instant livraisonIso = Instant.ofEpochMilli(livr);

        CommandDto commandDto = CommandDto.builder()
                .commandDate(todayIso)
                .deliveryDate(livraisonIso)
                .memberId(1L)
                .status(1L)
                .adressId(null)
                .build();
        //When
        assertThrows(IllegalArgumentException.class, () -> commandServiceInterface.createCommand(commandDto));
    }

    @Test
    void findOneById() {
        assertNotNull(commandRepositoryTest.findCommandById(3L));
    }

    @Test
    void findWithBadId() {
        assertThrows(CommandRequestExceptions.class, () -> commandServiceInterface.findOneById(3L));
    }

    @Test
    void getAllCommands() {
        assertNotNull(commandServiceInterface.getAllCommands());
    }

    @Test
    void deleteByIdSuccess() {
        Command command = createValidCommand();
        Member member = creatValidMember();
        Adress adress = createValidAdress();
       CommandDto commandDto = CommandMapper.toDto(command);
        commandServiceInterface.delete(commandDto.getId());
        //verify that the delete method was called with argument 'Long id'
        verify(commandRepositoryTest, times(1)).deleteById(commandDto.getId());
        }

    @Test
    void countCommands() {
            //Given
        Iterable <Command> commands= commandServiceInterface.getAllCommands();
        List<Command> result =  StreamSupport.stream(commands.spliterator(), false)
                .collect(Collectors.toList());

       assertEquals(result.size(),commandServiceInterface.countCommands());
    }

    @Test
    void updateSuccess() {
        //Given
        Instant todayUpdated = Instant.now();
        long tsUpdated = todayUpdated.toEpochMilli();
        Instant todayIsoUpdated = Instant.ofEpochMilli(tsUpdated);

        Instant livraisonUpdated = Instant.now().plus(15, ChronoUnit.DAYS);
        long livrUpdated = livraisonUpdated.toEpochMilli();
        Instant livraisonIsoUpdated = Instant.ofEpochMilli(livrUpdated);

        Command command = createValidCommand();
        CommandDto CommandDto = CommandMapper.toDto(command);
        when(commandRepositoryTest.findCommandById(command.getId())).thenReturn(Optional.of(command));

        Command alteredTestCommand = command;
        alteredTestCommand.setCommandDate(todayIsoUpdated);
        alteredTestCommand.setDeliveryDate(livraisonIsoUpdated);

        CommandDto alteredTestCommandDto = CommandMapper.toDto(alteredTestCommand);
        when(adressRepository.findAdressById(command.getId())).thenReturn(Optional.ofNullable(command.getAdress()));
        when(memberRepository.findMemberById(command.getId())).thenReturn(Optional.ofNullable(command.getMember()));

        // when -  action or the behaviour that we are going test
        Command updatedCommand = commandServiceInterface.updateCommand(alteredTestCommandDto);
        assertNotNull(updatedCommand);
        assertEquals(alteredTestCommand,updatedCommand);
    }

    @Test
    void updateFailedCommandAlreadyDelivered() {
        //Given
        Instant todayUpdated = Instant.now();
        long tsUpdated = todayUpdated.toEpochMilli();
        Instant todayIsoUpdated = Instant.ofEpochMilli(tsUpdated);

        Instant livraisonUpdated = Instant.now().minus(15, ChronoUnit.DAYS);
        long livrUpdated = livraisonUpdated.toEpochMilli();
        Instant livraisonIsoUpdated = Instant.ofEpochMilli(livrUpdated);

        Command command = createValidCommand();
        CommandDto CommandDto = CommandMapper.toDto(command);
        when(commandRepositoryTest.findCommandById(command.getId())).thenReturn(Optional.of(command));

        CommandDto testCommand = CommandDto;
        testCommand.setCommandDate(todayIsoUpdated);
        testCommand.setDeliveryDate(livraisonIsoUpdated);

        // when -  action or the behaviour that we are going test
        assertThrows(IllegalArgumentException.class , () -> commandServiceInterface.updateCommand(testCommand));
    }
}