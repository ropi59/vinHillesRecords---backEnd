package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.refund.RefundDTO;
import fr.insy2s.commerce.models.Refund;

import java.util.List;

public interface RefundServiceInterface {

    public List<RefundDTO> findAllRefund();

    public RefundDTO findRefundById(Long id);

    public RefundDTO createRefund(RefundDTO refundToCreateDTO);

    public Refund updateRefund(RefundDTO refundToUpdateDTO, Long id);

    public Refund deleteRefund(RefundDTO refundToDeleteDTO, Long id);
}
