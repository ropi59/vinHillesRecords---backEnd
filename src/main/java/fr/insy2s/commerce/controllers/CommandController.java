package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.command.CommandDto;
import fr.insy2s.commerce.dtos.member.MemberDTO;
import fr.insy2s.commerce.exceptions.CommandRequestExceptions;
import fr.insy2s.commerce.models.Command;
import fr.insy2s.commerce.models.Status;
import fr.insy2s.commerce.services.interfaces.CommandServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost/")
@RequestMapping("command")
@RequiredArgsConstructor
public class CommandController {

    private final CommandServiceInterface commandService;

    /**
     * CREATING COMMAND
     * @param commandDto as information of command
     * @return saved Command dto
     */
    //TODO enlever les try and catch
    @PostMapping("/saveCommand")
    public ResponseEntity<CommandDto> createCommand(@RequestBody CommandDto commandDto){
        ResponseEntity <CommandDto> commandDtoResponseEntity;
            commandService.createCommand(commandDto);
           if(commandDto != null){
               commandDtoResponseEntity = ResponseEntity.status(HttpStatus.CREATED).body(commandDto);
           }
           else{
               throw new CommandRequestExceptions("commandDto is null");
           }
        return  commandDtoResponseEntity ;
    }

    /**
     * UPDATING
     * @param commandDto information of a command
     * @return response entity
     */
    @GetMapping("/updateCommand")
    public ResponseEntity<String> updateCommand(@RequestBody CommandDto commandDto){
        if(commandDto.getId() == null){
            throw new IllegalArgumentException("Id is null");
        }
        commandService.updateCommand(commandDto);
        return new ResponseEntity<>("Customer update completed", HttpStatus.OK);
    }

    /**
     * Get all commands
     * @return iterable of Command
     */
    @GetMapping("/getAllCommand")
    public ResponseEntity<List<Command>>  getAllCommands(){
        return new ResponseEntity<>(commandService.getAllCommands(),HttpStatus.OK);
    }

    /**
     *DELETING
     * @param commandDto information of a command
     * @return response entity
     */
    @GetMapping("/deleteCommand")
    public ResponseEntity<String> deleteCommand(@RequestBody CommandDto commandDto){
        if(commandDto != null){
            commandService.delete(commandDto.getId());
        }else{
            throw new IllegalArgumentException("Id is null");
        }
        return new ResponseEntity<>("Customer got deleted", HttpStatus.OK);
    }

    /**
     * find the count of commands
     */
    @GetMapping("/getCountOfCommands")
    public Long getCountCommand(){
        return commandService.countCommands();
    }

    /**
     *
     * @param status of a command
     * @param memberDTO information of a member
     * @return list of commandDTO
     */
    @GetMapping("/getCommandByMemberAndStatus")
    public List<CommandDto> getCommandByMemberAndStatus(Status status, MemberDTO memberDTO){
        return commandService.findByMemberAndStatus(status,memberDTO);
    }
}

