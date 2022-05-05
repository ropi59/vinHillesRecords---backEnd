package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.models.Command;
import fr.insy2s.commerce.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    Optional<Command> findCommandById(Long id);
    List<CommandDto> findByMemberAndStatus(MemberDTO memberDTO, Status status);
}
