package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.AdressMapper;
import fr.insy2s.commerce.dtos.adress.AdressDTONoId;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.repositories.AdressRepository;
import fr.insy2s.commerce.services.interfaces.AdressServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdressService implements AdressServiceInterface {

    private final AdressRepository adressRepository;
    private final AdressMapper adressMapper;

    public AdressService(AdressRepository adressRepository, AdressMapper adressMapper) {
        this.adressRepository = adressRepository;
        this.adressMapper = adressMapper;
    }

    /**
     * method to save and adress
     * @param adressDtoNoIdToSave as adressToSave information
     * @return resultAdressDTO
     */
    @Override
    public AdressDTONoId save(AdressDTONoId adressDtoNoIdToSave) {
        AdressDTONoId resultAdressDTONoId = null;
        String streetNumber = adressDtoNoIdToSave.getStreetNumber();
        String pattern = "[0-9]{1,5}";
        boolean result = streetNumber.matches(pattern);
        if(adressDtoNoIdToSave != null && adressDtoNoIdToSave.isValid() && result) {
                Adress adress = this.adressMapper.toAdressBis(adressDtoNoIdToSave);
                adress = this.adressRepository.save(adress);
                resultAdressDTONoId = AdressMapper.toAdressDto(adress);
        }
        return resultAdressDTONoId;
    }

    /**
     * Update existing adress data
     * @param adressToUpdate adress data to update
     * @return adress
     * @throws NoSuchElementException if adress is not found
     * @throws IllegalArgumentException if data aren't valid
     * @throws NullPointerException if adress id not exist
     */
    @Override
    public Adress update(AdressDTONoId adressToUpdate, Long id)
            throws NoSuchElementException {
        Adress adress = this.adressRepository.findAdressById(id).orElse(null);
        if(adress == null) {
            throw new NoSuchElementException("adress update : unknown adress");
        }
        if(!adressToUpdate.isValid()){
                throw new IllegalArgumentException("adress update : invalid parameter");
        }else {
            adress = this.adressRepository.save(AdressMapper.toAdress(adressToUpdate));
        }
        return adress;
    }

    /**
     * method to delete an adress
     * @param id of adress
     * @throws NoSuchElementException if id is not known
     */
    @Override
    public void delete(Long id) throws NoSuchElementException{
        Adress adress = this.adressRepository.findAdressById(id).orElse(null);
        if(adress == null) {
            throw new NoSuchElementException("adress delete : unknown adress");
        }
        this.adressRepository.deleteById(id);
    }

    /**
     * search all adress
     * @return adressDTOList
     */
    @Override
    public List<AdressDTONoId> findAll() {
        List<Adress> adressList;
        adressList = this.adressRepository.findAll();
        List<AdressDTONoId> adressDTONoIdList;
        adressDTONoIdList = adressList.stream().map(AdressMapper :: toAdressDto).collect(Collectors.toList());
        return adressDTONoIdList;
    }

    /**
     * search adress by id
     * @param id adress 's id
     * @return adressDTO
     */
    @Override
    public AdressDTONoId findAdressById(Long id) {
        AdressDTONoId adressDTONoId;
        if(id != null){
            Optional<Adress> optionalAdress = this.adressRepository.findAdressById(id);
            adressDTONoId = optionalAdress.map(AdressMapper :: toAdressDto).orElse(null);
        }else {
            adressDTONoId = null;
        }
        return adressDTONoId;
    }
}
