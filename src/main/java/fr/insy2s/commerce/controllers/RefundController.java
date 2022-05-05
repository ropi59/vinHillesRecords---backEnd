package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.refund.RefundDTO;
import fr.insy2s.commerce.models.Refund;
import fr.insy2s.commerce.services.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost/")
@RequestMapping("refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {this.refundService = refundService; }

    /**
     * Method to get all refunds
     * @return list of all refunds
     */
    @GetMapping()
    public List<RefundDTO> findAllRefund() {
        return this.refundService.findAllRefund();
    }

    /**
     * Method to get a refund by id
     * @param id of a refund
     * @return refundDTO
     */
    @GetMapping("{id}")
    public ResponseEntity<RefundDTO> findRefundById(@PathVariable Long id){
        ResponseEntity<RefundDTO> result;
        try {
            RefundDTO refundDTO = this.refundService.findRefundById(id);
            if (refundDTO != null)
                result = ResponseEntity.ok(refundDTO);
            else
                result = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to create a refund in database
     * @param refundToCreateDTO refund information to save
     * @return RefundDTO
     */
    @PostMapping()
    public ResponseEntity<RefundDTO> createRefund(@RequestBody RefundDTO refundToCreateDTO) {
        ResponseEntity<RefundDTO> result;
        try {
            RefundDTO refundDTO = this.refundService.createRefund(refundToCreateDTO);
            if (refundDTO != null)
                result = ResponseEntity.status(HttpStatus.CREATED).body(refundDTO);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method for delete a refund request
     * @param refundToDelete refund's information to delete
     * @param id of the refund
     * @return refund
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Refund> deleteRefund(@RequestBody RefundDTO refundToDelete, @PathVariable Long id) {
        ResponseEntity<Refund> result;
        try {
            Refund refund = this.refundService.deleteRefund(refundToDelete, id);
            if (refund != null)
                result = ResponseEntity.status(HttpStatus.OK).body(refund);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to update a refund
     * @param refundToUpdateDTO information to update
     * @param id refund's id
     * @return refund updated
     */
    @PutMapping("{id}")
    public ResponseEntity<Refund> updateRefund(@RequestBody RefundDTO refundToUpdateDTO, @PathVariable Long id){
        ResponseEntity<Refund> result;
        try {
            Refund refund = this.refundService.updateRefund(refundToUpdateDTO, id);
            if (refund != null)
                result = ResponseEntity.status(HttpStatus.OK).body(refund);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }
}
