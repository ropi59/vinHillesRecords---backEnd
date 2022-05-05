package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.adress.AdressDTONoId;
import fr.insy2s.commerce.models.Adress;

import java.util.List;

public interface AdressServiceInterface {

     AdressDTONoId save(AdressDTONoId adress);

     Adress update(AdressDTONoId adress, Long id);

     void delete(Long id);

     List<AdressDTONoId> findAll();

     AdressDTONoId findAdressById(Long id);
}
