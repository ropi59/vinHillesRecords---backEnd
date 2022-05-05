package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.refund.RefundDTO;
import fr.insy2s.commerce.models.Refund;
import fr.insy2s.commerce.services.CommandLineService;
import fr.insy2s.commerce.services.CommandService;
import fr.insy2s.commerce.services.ProductService;

public final class RefundMapper {

    private RefundMapper() {
        //Private constructor for sonarqube
    }

    /**
     * Convert an entity into refundDTO
     * @param refund is an entity
     * @return a refundDTO
     */
    public static RefundDTO refundToRefundDTO(Refund refund){
        RefundDTO refundDTO = new RefundDTO();
        refundDTO.setId(refund.getId());
        refundDTO.setStatus(refund.getStatus());
        refundDTO.setAskingDate(refund.getAskingDate());
        refundDTO.setReason(refund.getReason());
        refundDTO.setCommandLineId(refund.getCommandLine().getId());
        return refundDTO;
    }

    /**
     * Convert a refundDTO into an entity
     * @param refundDTO is a DTO of refund
     * @return a refund entity
     */
    public static Refund refundDTOtoRefund(RefundDTO refundDTO, ProductService productService, CommandService commandService, CommandLineService commandLineService){
        Refund refund = new Refund();
        refund.setId(refundDTO.getId());
        refund.setStatus(refundDTO.getStatus());
        refund.setAskingDate(refundDTO.getAskingDate());
        refund.setReason(refundDTO.getReason());
        refund.setCommandLine(CommandLineMapper.commandLineDTOToCommandLine(commandLineService.findCommandLineById(refundDTO.getCommandLineId()), productService, commandService));
        return refund;
    }
}
