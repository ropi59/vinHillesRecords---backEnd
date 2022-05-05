package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.adress.AdressDTONoId;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdressMapper {

    @Autowired
    private final MemberRepository memberRepository;

    public AdressMapper(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * convert an adress to adressDTO
     * @param adress is an adress got in database
     * @return a adressDTO
     */
    public static AdressDTONoId toAdressDto(Adress adress){
        AdressDTONoId adressDTONoId = new AdressDTONoId();
        adressDTONoId.setStreetNumber(adress.getStreetNumber());
        adressDTONoId.setStreetName(adress.getStreetName());
        adressDTONoId.setZipCode(adress.getZipCode());
        adressDTONoId.setCity(adress.getCity());
        adressDTONoId.setCountry(adress.getCountry());
        adressDTONoId.setMembersIds(adress.getMemberList().stream().map(Member :: getId).collect(Collectors.toList()));

        return adressDTONoId;
    }

    /**
     * convert an adressDTO to adress
     * @param adressDTONoId as information of an adress
     * @return an adress
     */
    public static Adress toAdress(AdressDTONoId adressDTONoId) {

        Adress adress = new Adress();
        adress.setStreetNumber(adressDTONoId.getStreetNumber());
        adress.setStreetName(adressDTONoId.getStreetName());
        adress.setZipCode(adressDTONoId.getZipCode());
        adress.setCity(adressDTONoId.getCity());
        adress.setCountry(adressDTONoId.getCountry());

        /*adress.setMemberList(memberRepository.findMemberById(adress()).stream().map(Member::new).collect(Collectors.toList()));
        List<Long> memberIds = adressDTO.getMembersIds();
        Long firstElement = null;
        if (!memberIds.isEmpty() && memberIds.size() > 0) {
            firstElement = memberIds.get(0);
        }
        Member member = memberRepository.findMemberById(firstElement).orElse(null);
        List<Member> memberList = memberIds.stream().map(id -> memberRepository.findMemberById(id).orElse(null)).collect(Collectors.toList());
        adress.setMemberList(memberList);*/
        return adress;

    }

    public  Adress toAdressBis(AdressDTONoId adressDTONoId){
        Adress adress = new Adress();
        adress.setStreetNumber(adressDTONoId.getStreetNumber());
        adress.setStreetName(adressDTONoId.getStreetName());
        adress.setZipCode(adressDTONoId.getZipCode());
        adress.setCity(adressDTONoId.getCity());
        adress.setCountry(adressDTONoId.getCountry());

        //adress.setMemberList(memberRepository.findMemberById(adress()).stream().map(Member :: new).collect(Collectors.toList()));
        List<Long> memberIds = adressDTONoId.getMembersIds();
        Long firstElement = null;
        if (!memberIds.isEmpty()) {
            firstElement = memberIds.get(0);
        }
        Long finalFirstElement = firstElement;
        List<Member> memberList = memberIds.stream().map(id -> this.memberRepository.findMemberById(finalFirstElement).orElse(null)).collect(Collectors.toList());

        adress.setMemberList(memberList);
        return adress;
    }
}




