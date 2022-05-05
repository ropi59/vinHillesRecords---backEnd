package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.models.Command;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.Status;
import org.springframework.stereotype.Component;

@Component
public class CommandMapper {

    private CommandMapper() {
        //Private constructor for sonarqube
    }

    public static CommandDto toDto(Command entity){
        CommandDto dto = new CommandDto();
        if(entity.getId() != null ){
            dto.setId(entity.getId());
        }
        dto.setCommandDate(entity.getCommandDate());
        dto.setStatus(entity.getStatus().getId());
        dto.setDeliveryDate(entity.getDeliveryDate());
        dto.setMemberId(entity.getMember().getId());
        dto.setAdressId(entity.getAdress().getId());
        return dto;
    }

    public static Command toEntity(CommandDto dto, Member member, Adress adress){
        //Todo using an already made Status via enum
        Status status = new Status();
        status.setId(1l);
        status.setStatusName("Production");

        Command entity = new Command();
        entity.setId(dto.getId());
        entity.setCommandDate(dto.getCommandDate());
        entity.setStatus(status);
        entity.setDeliveryDate(dto.getDeliveryDate());
        entity.setMember(member);
        entity.setAdress(adress);
        return entity;
    }
}
