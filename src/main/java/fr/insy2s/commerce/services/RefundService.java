package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.RefundMapper;
import fr.insy2s.commerce.dtos.refund.RefundDTO;
import fr.insy2s.commerce.models.Refund;
import fr.insy2s.commerce.repositories.RefundRepository;
import fr.insy2s.commerce.services.interfaces.RefundServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RefundService implements RefundServiceInterface {

    private final RefundRepository refundRepository;
    private final ProductService productService;
    private final CommandService commandService;
    private final CommandLineService commandLineService;

    public RefundService(RefundRepository refundRepository, ProductService productService, CommandService commandService, CommandLineService commandLineService) {
        this.refundRepository = refundRepository;
        this.commandService = commandService;
        this.productService = productService;
        this.commandLineService = commandLineService;
    }

    /**
     * Method to get all refunds
     * @return a list of refundDTO
     */
    @Override
    public List<RefundDTO> findAllRefund() {
        List<Refund> refundList;
        refundList = this.refundRepository.findAll();

        List<RefundDTO> result;
        result = refundList.stream().map(RefundMapper::refundToRefundDTO).collect(Collectors.toList());
        return result;
    }

    /**
     * Method to get a refund by his id
     * @param id refund's id
     * @return refundDTO
     */
    @Override
    public RefundDTO findRefundById(Long id) {
        RefundDTO result;
        if (id != null){
            Optional<Refund> optionalRefund = refundRepository.findRefundById(id);
            result = optionalRefund.map(RefundMapper::refundToRefundDTO).orElse(null);
        }
        else
            result = null;
        return result;
    }

    /**
     * Method to create a refund request into database
     * @param refundToCreateDTO refund's information
     * @return refundDTO
     */
    @Override
    public RefundDTO createRefund(RefundDTO refundToCreateDTO) {
        RefundDTO result = null;
        if(refundToCreateDTO != null){
            Refund refund = this.refundRepository.save(RefundMapper.refundDTOtoRefund(refundToCreateDTO, productService, commandService, commandLineService));
           if (refund != null) {
               result = RefundMapper.refundToRefundDTO(refund);
           }
        }
        return result;
    }

    /**
     * Method to update existing refund data
     * @param refundToUpdateDTO refund data to update
     * @param id The id of refund to update
     * @return refund updated
     * @throws NoSuchElementException if refund is not found
     */
    @Override
    public Refund updateRefund(RefundDTO refundToUpdateDTO, Long id)
            throws NoSuchElementException {
        Refund refund = this.refundRepository.findRefundById(id).orElse(null);
        if (refund == null)
            throw new NoSuchElementException("Refund update : Unknown refund");
        if (Boolean.FALSE.equals(refundToUpdateDTO.validFields())) {
            throw new IllegalArgumentException("CommandLine update : Invalid parameter data");
        } else {
            refund = this.refundRepository.save(RefundMapper.refundDTOtoRefund(refundToUpdateDTO, productService, commandService, commandLineService));
        }
        return refund;
    }

    /**
     * Method to delete Refund
     * @param refundToDeleteDTO refund to delete
     * @param id of a refund
     * @return refund deleted
     * @throws NoSuchElementException if refund is not found
     */
    @Override
    public Refund deleteRefund(RefundDTO refundToDeleteDTO, Long id)
            throws NoSuchElementException {
        Refund refund = this.refundRepository.findRefundById(id).orElse(null);
        if (refund == null){
            throw new NoSuchElementException("Refund delete : Unknown refund");
        }
        this.refundRepository.delete(refund);
        return refund;
    }
}
