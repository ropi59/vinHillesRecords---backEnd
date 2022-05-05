package fr.insy2s.commerce.services;

import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.exceptions.CommandRequestExceptions;
import fr.insy2s.commerce.mappers.CommandMapper;
import fr.insy2s.commerce.models.Adress;
import fr.insy2s.commerce.models.Command;
import fr.insy2s.commerce.models.Member;
import fr.insy2s.commerce.models.Status;
import fr.insy2s.commerce.repositories.AdressRepository;
import fr.insy2s.commerce.repositories.CommandRepository;
import fr.insy2s.commerce.repositories.MemberRepository;
import fr.insy2s.commerce.services.interfaces.CommandServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandService implements CommandServiceInterface {

    private final CommandRepository commandRepository;
    private final MemberRepository memberRepository;
    private final AdressRepository adressRepository;
    private final MemberService memberService;
    private final AdressService adressService;

    @Override
    public CommandDto createCommand(CommandDto commandDto) {
        Instant today = Instant.now();
        long ts = today.toEpochMilli();
        Instant todayIso = Instant.ofEpochMilli(ts);
        if (commandDto.getDeliveryDate().isBefore(todayIso)) {
            throw new IllegalArgumentException("GetDelivery_date is Before today");
        }
        Optional<Adress> adressOptional = adressRepository.findAdressById(commandDto.getAdressId());
        Optional<Member> memberOptional = memberRepository.findMemberById(commandDto.getMemberId());
        Adress adress;
        Member member;
        if(adressOptional.isPresent()&&memberOptional.isPresent()){
            adress = adressOptional.get();
            member = memberOptional.get();
        }else{
            throw new IllegalArgumentException("addressOptional and or MemberOptional is null");
        }
        Command command = CommandMapper.toEntity(commandDto,member,adress);
        if (commandDto.getCommandDate() != null
                && commandDto.getDeliveryDate() != null
                && commandDto.getMemberId() != null
                && commandDto.getAdressId() != null
                && commandDto.getStatus() != null) {
                commandRepository.save(command);
        }else{
            throw new IllegalArgumentException("The command is lacking some attributes");
        }
        return CommandMapper.toDto(command);
    }

    //To Do try Update with Optionals !!
    @Override
    public Command updateCommand(CommandDto commandDto) {
        Instant today = Instant.now();
        long ts = today.toEpochMilli();
        Instant todayIso = Instant.ofEpochMilli(ts);
        Optional<Command> commandToUpdateOpt = commandRepository
                .findCommandById(commandDto.getId());
        Command commandToUpdate;
        if (commandToUpdateOpt.isPresent()) {
            commandToUpdate = commandToUpdateOpt.get();
        } else {
            throw new NullPointerException("commandToUpdate isn't present");
        }
        Optional<Adress> adressOptional = adressRepository.findAdressById(commandDto.getAdressId());
        Optional<Member> memberOptional = memberRepository.findMemberById(commandDto.getMemberId());
        Adress adress;
        Member member;
        if(adressOptional.isPresent()&&memberOptional.isPresent()){
            adress = adressOptional.get();
            member = memberOptional.get();
        }else{
            throw new IllegalArgumentException("addressOptional is null");
        }

        //TODO : CHANGE STATUS WITH ENUM

        if (!commandToUpdate.getStatus().getStatusName().equals("Livré")
                && !commandToUpdate.getStatus().getStatusName().equals("Livraison")
                && commandToUpdate.getDeliveryDate().isAfter(todayIso))
        {
            commandToUpdate.setCommandDate(commandDto.getCommandDate());
            commandToUpdate.setDeliveryDate(commandDto.getDeliveryDate());
            commandToUpdate.setAdress(adress);
            commandToUpdate.setMember(member);
        }
        else {
            throw new IllegalArgumentException("Pas possible de faire une changement! le colis est déjà livré ou en livraison");
        }
        return commandToUpdate;
    }

    @Override
    public CommandDto findOneById(Long id) {
       return commandRepository.findCommandById(id)
                       .map(command -> CommandMapper.toDto(command))
               .orElseThrow(() -> new CommandRequestExceptions("Command with id : "+id+" Not found"));
    }

    public List<Command> getAllCommands() {
        Iterable<Command> iterCommands = commandRepository.findAll();
        return StreamSupport.stream(iterCommands.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        commandRepository.deleteById(id);
    }

    public Long countCommands(){
        return commandRepository.count();
    }


    @Override
    public List<CommandDto> findByMemberAndStatus(Status status, MemberDTO memberDTO) {
        Iterable<CommandDto> iterCommandsDto = commandRepository.findByMemberAndStatus(memberDTO,status);
        return StreamSupport.stream(iterCommandsDto.spliterator(),false)
                .collect(Collectors.toList());
    }
}
