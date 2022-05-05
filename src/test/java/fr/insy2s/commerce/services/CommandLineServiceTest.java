package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.CommandLineMapper;
import fr.insy2s.commerce.dtos.commandline.CommandLineDTO;
import fr.insy2s.commerce.models.*;
import fr.insy2s.commerce.models.Tag;
import fr.insy2s.commerce.repositories.CommandLineRepository;
import fr.insy2s.commerce.repositories.CommandRepository;
import fr.insy2s.commerce.repositories.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandLineServiceTest {

    @MockBean
    private CommandLineRepository commandLineRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CommandRepository commandRepository;
    @Autowired
    private CommandLineService commandLineService;
    @Autowired
    private CommandService commandService;
    @Autowired
    private ProductService productService;

    @BeforeAll
    void init() {commandLineService = new CommandLineService(commandLineRepository, productService, commandService);}

    Instant today = Instant.now();
    List<Tag> tagList = new ArrayList<>();
    List<Adress> adressList = new ArrayList<>();

    //Convenient methods

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

    //Method findCommandLineById

    /**
     * Test findCommandLineById when commandLine is found
     */
    @DisplayName("Test findCommandLineById : commandLine found")
    @Test
    void testFindCommandLineByIdFound() {
        Product product = new Product();
        Command command = new Command();
        CommandLine commandLine = new CommandLine();
        commandLine.setId(1L);
        commandLine.setUnitePrice(19.99F);
        commandLine.setQuantity(1);
        commandLine.setProduct(product);
        commandLine.setCommand(command);
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLineDTO result = this.commandLineService.findCommandLineById(commandLine.getId());
        Assertions.assertNotNull(result);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Assertions.assertEquals(commandLineDTO, result);
    }

    /**
     * Test findCommandLineByID when commandLIne is not found
     */
    @DisplayName("Test findCommandLineById : commandLine not found")
    @Test
    void testFindCommandLineByIdNotFound() {
        Long id = 123456789L;
        BDDMockito.when(this.commandLineRepository.findCommandLineById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(this.commandLineService.findCommandLineById(id));
    }

    /**
     * Test findCommandLineById when id is invalid
     */
    @DisplayName("Test findCommandLineById : invalid id")
    @Test
    void testFindCommandLineByInvalidId() {
        Long id = 2992337203685477580L;
        Assertions.assertNull(this.commandLineService.findCommandLineById(id));
    }

    /**
     * Test findCommandLineById when id is null
     */
    @DisplayName("Test findCommandLineById : id null")
    @Test
    void testFindCommandLineByNullId() {
        Long id = null;
        Assertions.assertNull(this.commandLineService.findCommandLineById(id));
    }

    //Method createCommandLine

    /**
     * Test createCommandLineSuccessfully
     */
    @DisplayName("Test createCommandLine : creation successful")
    @Test
    void createCommandLineSuccessfully() {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineCreationDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        BDDMockito.when(productRepository.findById(commandLine.getProduct().getId())).thenReturn(Optional.ofNullable(commandLine.getProduct()));
        BDDMockito.when(commandRepository.findCommandById(commandLine.getCommand().getId())).thenReturn(Optional.ofNullable(commandLine.getCommand()));
        BDDMockito.when(commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(commandLine);
        CommandLineDTO result = this.commandLineService.createCommandLine(commandLineCreationDTO);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(commandLineDTO, result);
    }

    /**
     * Test createCommandLine when commandLineDTO is null
     */
    @DisplayName("Test createCommandLine : commandLineDTO null")
    @Test
    void testCreateCommandLineNullDTO() {
        CommandLineDTO commandLineDTO = Assertions.assertDoesNotThrow(() -> this.commandLineService.createCommandLine(null));
        Assertions.assertNull(commandLineDTO);
    }

    /**
     * Test createCommandLine when id is null
     */
    @DisplayName("Test createCommandLine : id null")
    @Test
    void testCreateCommandLineNullId() {
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setId(null);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    /**
     * Test createCommandLine  when unitePrice is negative
     */
    @DisplayName("Test createCommandLine : negative unitePrice ")
    @Test
    void testCreateCommandLineNegativeUnitePrice(){
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setUnitePrice(-10F);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    /**
     * Test createCommandLine  when unitePrice is free
     */
    @DisplayName("Test createCommandLine : free unitePrice ")
    @Test
    void testCreateCommandLineFreeUnitePrice(){
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setUnitePrice(0F);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    /**
     * Test createCommandLine  when quantity is negative
     */
    @DisplayName("Test createCommandLine : negative quantity ")
    @Test
    void testCreateCommandLineNegativeQuantity(){
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setQuantity(-10);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    /**
     * Test createCommandLine  when quantity is 0
     */
    @DisplayName("Test createCommandLine : no quantity ")
    @Test
    void testCreateCommandLineQuantity0(){
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setQuantity(0);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    /**
     * Test createCommandLine when productId is null
     */
    @DisplayName("Test createCommandLine : productId null")
    @Test
    void testCreateCommandLineProductIdNull(){
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setProductId(null);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    /**
     * Test createCommandLine when commandId is null
     */
    @DisplayName("Test createCommandLine : commandId null")
    @Test
    void testCreateCommandLineCommandIdNull(){
        CommandLine commandLine = createValidCommandLine();
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        commandLineDTO.setCommandId(null);
        Assertions.assertNull(this.commandLineService.createCommandLine(commandLineDTO));
    }

    //Method updateCommandLine

    /**
     * Test updateCommandLine new unitePrice
     */
    @DisplayName("Test updateCommandLine : new unitePrice")
    @Test
    void updateCommandLineWithNewUnitePrice() {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLine newCommandLine = commandLine;
        newCommandLine.setUnitePrice(10.99F);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(newCommandLine);
        BDDMockito.when(this.commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(newCommandLine);
        BDDMockito.when(productRepository.findById(commandLine.getProduct().getId())).thenReturn(Optional.ofNullable(commandLine.getProduct()));
        BDDMockito.when(commandRepository.findCommandById(commandLine.getCommand().getId())).thenReturn(Optional.ofNullable(commandLine.getCommand()));

        CommandLine result = null;
        try {
            result = this.commandLineService.updateCommandLine(commandLineDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newCommandLine, result);
        }
    }

    /**
     * Test updateCommandLine new quantity
     */
    @DisplayName("Test updateCommandLine : new quantity")
    @Test
    void updateCommandLineWithNewQuantity() {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLine newCommandLine = commandLine;
        newCommandLine.setQuantity(3);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(newCommandLine);
        BDDMockito.when(this.commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(newCommandLine);
        BDDMockito.when(productRepository.findById(commandLine.getProduct().getId())).thenReturn(Optional.ofNullable(commandLine.getProduct()));
        BDDMockito.when(commandRepository.findCommandById(commandLine.getCommand().getId())).thenReturn(Optional.ofNullable(commandLine.getCommand()));

        CommandLine result = null;
        try {
            result = this.commandLineService.updateCommandLine(commandLineDTO, id);
        }
        finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newCommandLine, result);
        }
    }

    /**
     * Test updateCommandLine with 0 unitePrice throw IllegalArgumentException
     */
    @DisplayName("Test updateCommandLine with 0 unitePrice throw IllegalArgumentException")
    @Test
    void testUpdateCommandLineWith0UnitePrice() {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLine newCommandLine = commandLine;
        newCommandLine.setUnitePrice(0);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(newCommandLine);
        BDDMockito.when(this.commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(newCommandLine);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.commandLineService.updateCommandLine(commandLineDTO, id));
    }

    /**
     * Test updateCommandLine with negative unitePrice throw IllegalArgumentException
     */
    @DisplayName("Test updateCommandLine with negative unitePrice throw IllegalArgumentException")
    @Test
    void testUpdateCommandLineWithNegativeUnitePrice() {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLine newCommandLine = commandLine;
        newCommandLine.setUnitePrice(-10);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(newCommandLine);
        BDDMockito.when(this.commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(newCommandLine);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.commandLineService.updateCommandLine(commandLineDTO, id));
    }

    /**
     * Test updateCommandLine with 0 unitePrice throw IllegalArgumentException
     */
    @DisplayName("Test updateCommandLine with 0 quantity throw IllegalArgumentException")
    @Test
    void testUpdateCommandLineWith0Quantity() {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLine newCommandLine = commandLine;
        newCommandLine.setQuantity(0);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(newCommandLine);
        BDDMockito.when(this.commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(newCommandLine);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.commandLineService.updateCommandLine(commandLineDTO, id));
    }

    /**
     * Test updateCommandLine with negative quantity throw IllegalArgumentException
     */
    @DisplayName("Test updateCommandLine with negative quantity throw IllegalArgumentException")
    @Test
    void testUpdateCommandLineWithNegativeQuantity() {
        Long id = 1L;
        CommandLine commandLine = createValidCommandLine();
        BDDMockito.when(this.commandLineRepository.findCommandLineById(commandLine.getId())).thenReturn(Optional.of(commandLine));
        CommandLine newCommandLine = commandLine;
        newCommandLine.setQuantity(-10);
        CommandLineDTO commandLineDTO = CommandLineMapper.commandLineToCommandLineDTO(newCommandLine);
        BDDMockito.when(this.commandLineRepository.save(ArgumentMatchers.any(CommandLine.class))).thenReturn(newCommandLine);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.commandLineService.updateCommandLine(commandLineDTO, id));
    }

    //Method deleteCommandLine

}
