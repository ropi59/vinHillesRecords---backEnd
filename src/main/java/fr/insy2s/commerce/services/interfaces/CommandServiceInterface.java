package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.models.Command;
import fr.insy2s.commerce.models.Status;

import java.util.List;

public interface CommandServiceInterface {

    CommandDto createCommand(CommandDto commandDto);

    Command updateCommand(CommandDto commandDto);

    CommandDto findOneById(Long id);

    List<Command> getAllCommands();

    void delete(Long id);

    Long countCommands();

    List<CommandDto> findByMemberAndStatus(Status status, MemberDTO memberDTO);
}
