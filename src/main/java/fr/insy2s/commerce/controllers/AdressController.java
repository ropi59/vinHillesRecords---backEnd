package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.adress.AdressDTONoId;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.services.AdressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost/")
@RequestMapping("adress")
public class AdressController {

    private final AdressService adressService;

    public AdressController(AdressService adressService) {
        this.adressService = adressService;
    }

    /**
     * methode to create adress
     * @param adressToCreate adress for delivery
     * @return responseAdressDTO
     */
    @PostMapping()
    public ResponseEntity<AdressDTONoId> createAdress(@RequestBody AdressDTONoId adressToCreate) {
        ResponseEntity<AdressDTONoId> responseAdressDTO;
        try {
            AdressDTONoId adressDTONoId = this.adressService.save(adressToCreate);
            if (adressDTONoId != null) {
                responseAdressDTO = ResponseEntity.status(HttpStatus.CREATED).body(adressDTONoId);
            } else {
                responseAdressDTO = ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            responseAdressDTO = ResponseEntity.badRequest().build();
        }
        return responseAdressDTO;
    }

    /**
     * method to update adress
     * @param adressToUpdate as the adress information to update
     * @param id as the id of the adress to update
     * @return responseUpdateAdress
     */
    @PutMapping("{id}")
    public ResponseEntity<Adress> updateAdress(@RequestBody AdressDTONoId adressToUpdate, @PathVariable Long id) {
        ResponseEntity<Adress> responseUpdateAdress;
        try {
            Adress adress = this.adressService.update(adressToUpdate, id);
            if (adress != null) {
                responseUpdateAdress = ResponseEntity.status(HttpStatus.OK).body(adress);
            } else {
                responseUpdateAdress = ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            responseUpdateAdress = ResponseEntity.badRequest().build();
        }
        return responseUpdateAdress;
    }

    /**
     * method to delete an adress
     * @param id of an adress to delete
     * @return response
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAdress(@PathVariable Long id) {
        ResponseEntity<Void> response;
        try {
            this.adressService.delete(id);
            response = ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("{id}")
    public ResponseEntity<AdressDTONoId> adressFindById(@PathVariable Long id) {
        ResponseEntity<AdressDTONoId> response;
        try {
            AdressDTONoId adressDTONoId = this.adressService.findAdressById(id);
            response = ResponseEntity.ok(adressDTONoId);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().build();
        }

        return response;
    }

    @GetMapping()
    public ResponseEntity<List<AdressDTONoId>> findAllAdress() {
        ResponseEntity<List<AdressDTONoId>> adressDTOList;
        try {
            this.adressService.findAll();
            adressDTOList = ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            adressDTOList = ResponseEntity.badRequest().build();

        }
        return adressDTOList;
    }
}