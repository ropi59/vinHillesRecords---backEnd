package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.commandline.CommandLineDTO;
import fr.insy2s.commerce.models.CommandLine;
import fr.insy2s.commerce.services.CommandLineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost")
@RequestMapping("commandlines")
public class CommandLineController {

    private final CommandLineService commandLineService;

    public CommandLineController(CommandLineService commandLineService){
        this.commandLineService = commandLineService;
    }

    /**
     * Method to get all commandLines
     * @return list of all commandLines
     */
    @GetMapping()
    public List<CommandLineDTO> findAllCommandLine(){
        return this.commandLineService.findAllCommandLine();
    }

    /**
     * Method to get a commandLine by id
     * @param id of the commandLine
     * @return CommandLineDTO
     */
    @GetMapping("{id}")
    public ResponseEntity<CommandLineDTO> findCommandLineById(@PathVariable Long id){
        ResponseEntity<CommandLineDTO> result;
        try {
            CommandLineDTO commandLineDTO = this.commandLineService.findCommandLineById(id);
            if(commandLineDTO != null)
                result = ResponseEntity.ok(commandLineDTO);
            else
                result = ResponseEntity.notFound().build();
        }
        catch (Exception e){
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to create a commandLine in database
     * @param commandLineCreateDTO commandLine information to save
     * @return CommandLineDTO
     */
    @PostMapping()
    public ResponseEntity<CommandLineDTO> createCommandLine(@RequestBody CommandLineDTO commandLineCreateDTO){
        ResponseEntity<CommandLineDTO> result;
        try {
            CommandLineDTO commandLineDTO = this.commandLineService.createCommandLine(commandLineCreateDTO);
            if (commandLineDTO != null)
                result = ResponseEntity.status(HttpStatus.CREATED).body(commandLineDTO);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /***
     * Method to update commandLine in database
     * @param commandLineUpdateDTO information to update
     * @return commandLine updated
     */
    @PutMapping("{id}")
    public ResponseEntity<CommandLine> updateCommandLine(@RequestBody CommandLineDTO commandLineUpdateDTO, @PathVariable Long id){
        ResponseEntity<CommandLine> result;
        try{
            CommandLine commandLine = this.commandLineService.updateCommandLine(commandLineUpdateDTO, id);
            if(commandLine != null)
                result = ResponseEntity.status(HttpStatus.OK).body(commandLine);
            else
                result = ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }

    /**
     * Method to delete a commandLine by id
     * @param commandLineDTO to delete
     * @return always true
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(@RequestBody CommandLineDTO commandLineDTO){
        this.commandLineService.deleteCommandLineById(commandLineDTO.getId());
        return ResponseEntity.ok(true);
    }
}
