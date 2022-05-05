package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.RefundMapper;
import fr.insy2s.commerce.dtos.refund.RefundDTO;
import fr.insy2s.commerce.models.*;
import fr.insy2s.commerce.models.Tag;
import fr.insy2s.commerce.repositories.CommandLineRepository;
import fr.insy2s.commerce.repositories.CommandRepository;
import fr.insy2s.commerce.repositories.ProductRepository;
import fr.insy2s.commerce.repositories.RefundRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RefundServiceTest {

    @MockBean
    private RefundRepository refundRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CommandLineRepository commandLineRepository;
    @MockBean
    private CommandRepository commandRepository;
    @Autowired
    private RefundService refundService;
    @Autowired
    private CommandService commandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommandLineService commandLineService;

    @BeforeAll
    void init() {refundService = new RefundService(refundRepository, productService, commandService, commandLineService);}

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

    //Method findRefundById

    /**
     * Test findRefundById when refund is found
     */
    @DisplayName("Test findRefundById : refund found")
    @Test
    void testFindRefundByIdRefundFound(){
        CommandLine commandLine = new CommandLine();
        Refund refund = new Refund();
        refund.setId(1L);
        refund.setStatus("Accepted");
        refund.setAskingDate(today);
        refund.setReason("Reason for why i'm asking for refund");
        refund.setCommandLine(commandLine);
        BDDMockito.when(this.refundRepository.findRefundById(refund.getId())).thenReturn(Optional.of(refund));
        RefundDTO result = this.refundService.findRefundById(refund.getId());
        Assertions.assertNotNull(result);
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        Assertions.assertEquals(refundDTO, result);
    }

    /**
     * Test findRefundById when refund is not found
     */
    @DisplayName("Test findRefundById : refund not found")
    @Test
    void testFindRefundByIdNotFound() {
        Long id = 123456789L;
        BDDMockito.when(this.refundRepository.findRefundById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(this.refundService.findRefundById(id));
    }

    /**
     * Test findRefundById when id is invalid
     */
    @DisplayName("Test findRefundById : invalid id")
    @Test
    void testFindRefundByInvalidId() {
        Long id = 2992337203685477580L;
        Assertions.assertNull(this.refundService.findRefundById(id));
    }

    /**
     * Test findRefundById when id is null
     */
    @DisplayName("Test findRefundById : null id")
    @Test
    void testFindRefundByNullId() {
        Long id = null;
        Assertions.assertNull(this.refundService.findRefundById(id));
    }

    //Method createRefund

    /**
     * Test createRefund successful
     */
    @DisplayName("Test createRefund : creation successful")
    @Test
    void testCreateRefundSuccessful() {
        Refund refund = createValidRefund();
        RefundDTO refundCreationDTO = RefundMapper.refundToRefundDTO(refund);
        BDDMockito.when(productRepository.findById(refund.getCommandLine().getProduct().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(commandRepository.findCommandById(refund.getCommandLine().getCommand().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        BDDMockito.when(commandLineRepository.findCommandLineById(refund.getCommandLine().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine()));
        BDDMockito.when(refundRepository.save(ArgumentMatchers.any(Refund.class))).thenReturn(refund);
        RefundDTO result = this.refundService.createRefund(refundCreationDTO);
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(refundDTO, result);
    }

    /**
     * Test createRefund when refundDTO is null
     */
    @DisplayName("Test createRefund : refundDTO is null")
    @Test
    void testCreateRefundNullDTO () {
        RefundDTO refundDTO = Assertions.assertDoesNotThrow(() -> this.refundService.createRefund(null));
        Assertions.assertNull(refundDTO);
    }

    /**
     * Test createRefund when id is null
     */
    @DisplayName("Test createRefund : id null")
    @Test
    void testCreateRefundIdNull () {
        Refund refund = createValidRefund();
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        refundDTO.setId(null);
        BDDMockito.when(this.commandLineRepository.findCommandLineById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine()));
        BDDMockito.when(this.productRepository.findById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        Assertions.assertNull(this.refundService.createRefund(refundDTO));
    }

    /**
     * Test createRefund when status is null
     */
    @DisplayName("Test createRefund : status null")
    @Test
    void testCreateRefundNullStatus() {
        Refund refund = createValidRefund();
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        refundDTO.setStatus(null);
        BDDMockito.when(this.commandLineRepository.findCommandLineById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine()));
        BDDMockito.when(this.productRepository.findById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        Assertions.assertNull(this.refundService.createRefund(refundDTO));
    }

    /**
     * Test createRefund when asking date is null
     */
    @DisplayName("Test createRefund : asking date null")
    @Test
    void testCreateRefundNullAskingDate() {
        Refund refund = createValidRefund();
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        refundDTO.setAskingDate(null);
        BDDMockito.when(this.commandLineRepository.findCommandLineById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine()));
        BDDMockito.when(this.productRepository.findById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        Assertions.assertNull(this.refundService.createRefund(refundDTO));
    }

    /**
     * Test createRefund when reason is null
     */
    @DisplayName("Test createRefund : reason null")
    @Test
    void testCreateRefundNullReason() {
        Refund refund = createValidRefund();
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        refundDTO.setReason(null);
        BDDMockito.when(this.commandLineRepository.findCommandLineById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine()));
        BDDMockito.when(this.productRepository.findById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        Assertions.assertNull(this.refundService.createRefund(refundDTO));
    }

    /**
     * Test createRefund when id is invalid
     */
    @DisplayName("Test createRefund : invalid id")
    @Test
    void testCreateRefundInvalidId () {
        Refund refund = createValidRefund();
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(refund);
        refundDTO.setId(2992337203685477580L);
        BDDMockito.when(this.commandLineRepository.findCommandLineById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine()));
        BDDMockito.when(this.productRepository.findById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(1L)).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        Assertions.assertNull(this.refundService.createRefund(refundDTO));
    }

    //Method updateRefund

    /**
     * Test UpdateRefund new reason
     */
    @DisplayName("Test updateRefund : new reason")
    @Test
    void testUpdateRefundWithNewReason() {
        Long id = 1L;
        Refund refund = createValidRefund();
        BDDMockito.when(this.refundRepository.findRefundById(refund.getId())).thenReturn(Optional.of(refund));
        Refund newRefund = refund;
        newRefund.setReason("i change my advice");
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(newRefund);
        BDDMockito.when(this.refundRepository.save(ArgumentMatchers.any(Refund.class))).thenReturn(newRefund);
        BDDMockito.when(this.productRepository.findById(refund.getCommandLine().getProduct().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(refund.getCommandLine().getCommand().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        BDDMockito.when(this.commandLineRepository.findCommandLineById(refund.getCommandLine().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine()));

        Refund result = null;
        try {
            result = this.refundService.updateRefund(refundDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newRefund, result);
        }
    }

    /**
     * Test UpdateRefund new status
     */
    @DisplayName("Test updateRefund : new status")
    @Test
    void testUpdateRefundWithNewStatus() {
        Long id = 1L;
        Refund refund = createValidRefund();
        BDDMockito.when(this.refundRepository.findRefundById(refund.getId())).thenReturn(Optional.of(refund));
        Refund newRefund = refund;
        newRefund.setStatus("refused");
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(newRefund);
        BDDMockito.when(this.refundRepository.save(ArgumentMatchers.any(Refund.class))).thenReturn(newRefund);
        BDDMockito.when(this.productRepository.findById(refund.getCommandLine().getProduct().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(refund.getCommandLine().getCommand().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        BDDMockito.when(this.commandLineRepository.findCommandLineById(refund.getCommandLine().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine()));

        Refund result = null;
        try {
            result = this.refundService.updateRefund(refundDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newRefund, result);
        }
    }

    /**
     * Test UpdateRefund new commandLine
     */
    @DisplayName("Test updateRefund : new commandLIne")
    @Test
    void testUpdateRefundWithNewCommandLine() {
        Long id = 1L;
        Role role = new Role(3L, "customer");
        Product product = new Product(1L, "title", "artist", "description", 19.99F, "label", "format", 10, "coverUrl", today, today, tagList);
        Member member = new Member(1L, "john", "doe", "johndoe@mail.com", "010203040506", "azerty", true, "resetKey", "avatarurl", role, adressList);
        Adress adress = new Adress(1L, "1", "main street", "95000", "Paris", "France");
        Status status = new Status(1L, "delivery");
        Command command = new Command(1L, today, status, today, member, adress);
        Refund refund = createValidRefund();
        BDDMockito.when(this.refundRepository.findRefundById(refund.getId())).thenReturn(Optional.of(refund));
        Refund newRefund = refund;
        CommandLine commandLine = new CommandLine(1L, 49.99F , 1, product, command);
        newRefund.setCommandLine(commandLine);
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(newRefund);
        BDDMockito.when(this.refundRepository.save(ArgumentMatchers.any(Refund.class))).thenReturn(newRefund);
        BDDMockito.when(this.productRepository.findById(refund.getCommandLine().getProduct().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getProduct()));
        BDDMockito.when(this.commandRepository.findCommandById(refund.getCommandLine().getCommand().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine().getCommand()));
        BDDMockito.when(this.commandLineRepository.findCommandLineById(refund.getCommandLine().getId())).thenReturn(Optional.ofNullable(refund.getCommandLine()));

        Refund result = null;
        try {
            result = this.refundService.updateRefund(refundDTO, id);
        } finally {
            Assertions.assertNotNull(result);
            Assertions.assertEquals(newRefund, result);
        }
    }

    /**
     * Test updateRefund with invalid reason
     */
    @DisplayName("Test updateRefund with invalid reason throw IllegalArgumentException")
    @Test
    void testUpdateReasonWithInvalidReason() {
        Long id = 1L;
        Refund refund = createValidRefund();
        BDDMockito.when(this.refundRepository.findRefundById(refund.getId())).thenReturn(Optional.of(refund));
        Refund newRefund = refund;
        newRefund.setReason("");
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(newRefund);
        BDDMockito.when(this.refundRepository.save(ArgumentMatchers.any(Refund.class))).thenReturn(newRefund);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.refundService.updateRefund(refundDTO, id));
    }

    /**
     * Test updateRefund with invalid status
     */
    @DisplayName("Test updateRefund with invalid status throw IllegalArgumentException")
    @Test
    void testUpdateReasonWithInvalidStatus() {
        Long id = 1L;
        Refund refund = createValidRefund();
        BDDMockito.when(this.refundRepository.findRefundById(refund.getId())).thenReturn(Optional.of(refund));
        Refund newRefund = refund;
        newRefund.setStatus("");
        RefundDTO refundDTO = RefundMapper.refundToRefundDTO(newRefund);
        BDDMockito.when(this.refundRepository.save(ArgumentMatchers.any(Refund.class))).thenReturn(newRefund);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.refundService.updateRefund(refundDTO, id));
    }
}
