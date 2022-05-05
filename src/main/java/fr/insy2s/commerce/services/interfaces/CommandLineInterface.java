package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.commandline.CommandLineDTO;
import fr.insy2s.commerce.models.CommandLine;

import java.util.List;

public interface CommandLineInterface {

    public List<CommandLineDTO> findAllCommandLine();

    public CommandLineDTO findCommandLineById(Long id);

    public CommandLineDTO createCommandLine(CommandLineDTO commandLineToCreateDTO);

    public CommandLine updateCommandLine(CommandLineDTO commandLineToUpdateDTO, Long id);

    public void deleteCommandLineById(Long id);
}
