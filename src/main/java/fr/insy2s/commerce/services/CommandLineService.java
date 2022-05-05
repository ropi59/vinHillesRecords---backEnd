package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.CommandLineMapper;
import fr.insy2s.commerce.dtos.commandline.CommandLineDTO;
import fr.insy2s.commerce.models.CommandLine;
import fr.insy2s.commerce.repositories.CommandLineRepository;
import fr.insy2s.commerce.services.interfaces.CommandLineInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandLineService implements CommandLineInterface {

    private final CommandLineRepository commandLineRepository;
    private final ProductService productService;
    private final CommandService commandService;

    public CommandLineService(CommandLineRepository commandLineRepository, ProductService productService, CommandService commandService) {
        this.commandLineRepository = commandLineRepository;
        this.productService = productService;
        this.commandService = commandService;
    }

    /**
     * Method to get all commandLines
     * @return list of all CommandLineDTO
     */
    @Override
    public List<CommandLineDTO> findAllCommandLine() {
        List<CommandLine> commandLineList;
        commandLineList = this.commandLineRepository.findAll();

        List<CommandLineDTO> result;
        result = commandLineList.stream().map(CommandLineMapper::commandLineToCommandLineDTO).collect(Collectors.toList());
        return result;
    }

    /**
     * Method to find a commandLine by id
     * @param id commandLine id
     * @return commandLineDTO
     */
    @Override
    public CommandLineDTO findCommandLineById(Long id) {
        CommandLineDTO result;
        if (id != null){
            Optional<CommandLine> optionalCommandLine = commandLineRepository.findCommandLineById(id);
            result = optionalCommandLine.map(CommandLineMapper::commandLineToCommandLineDTO).orElse(null);
        }
        else
            result = null;
        return result;
    }

    /**
     * Method to save a commandLine entity into database
     * @param commandLineToCreateDTO commandLine information
     * @return commandLineDTO
     */
    @Override
    public CommandLineDTO createCommandLine(CommandLineDTO commandLineToCreateDTO) {
        CommandLineDTO result = null;
        if (commandLineToCreateDTO != null && commandLineToCreateDTO.isValid()){
            CommandLine commandLine = this.commandLineRepository.save(CommandLineMapper.commandLineDTOToCommandLine(commandLineToCreateDTO, productService, commandService));
            result = CommandLineMapper.commandLineToCommandLineDTO(commandLine);
        }
        return result;
    }

    /**
     * Method to update commandLine information
     * @param commandLineToUpdateDTO commandLine data to update
     * @param id the id of the commandLine to update
     * @return commandLine updated
     * @throws NoSuchElementException if commandLine is not found
     */
    @Override
    public CommandLine updateCommandLine(CommandLineDTO commandLineToUpdateDTO, Long id)
            throws NoSuchElementException, IllegalArgumentException {
        CommandLine commandLine = this.commandLineRepository.findCommandLineById(id).orElse(null);
        if (commandLine == null) {
            throw new NoSuchElementException("CommandLine update : Unknown commandLine");
        }
        if(!commandLineToUpdateDTO.isValid()){
            throw new IllegalArgumentException ("CommandLine update : Invalid parameter data");
        } else {
            commandLine = this.commandLineRepository.save(CommandLineMapper.commandLineDTOToCommandLine(commandLineToUpdateDTO,  productService, commandService));
        }
        return commandLine;
    }

    @Override
    @Transactional
    public void deleteCommandLineById(Long id) {
        this.commandLineRepository.deleteCommandLineById(id);
    }
}
