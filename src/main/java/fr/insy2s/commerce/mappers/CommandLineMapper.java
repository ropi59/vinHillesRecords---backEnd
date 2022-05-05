package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.commandline.CommandLineDTO;
import fr.insy2s.commerce.models.CommandLine;
import fr.insy2s.commerce.services.CommandService;
import fr.insy2s.commerce.services.ProductService;

public final class CommandLineMapper {

    private CommandLineMapper(){
    }

    /**
     * Convert a commandLine entity into a commandLineDTO
     * @param commandLine is an entity
     * @return a commandLineDTO
     */
    public static CommandLineDTO commandLineToCommandLineDTO(CommandLine commandLine){
        CommandLineDTO commandLineDTO = new CommandLineDTO();
        commandLineDTO.setId(commandLine.getId());
        commandLineDTO.setUnitePrice(commandLine.getUnitePrice());
        commandLineDTO.setQuantity(commandLine.getQuantity());
        commandLineDTO.setProductId(commandLine.getProduct().getId());
        commandLineDTO.setCommandId(commandLine.getCommand().getId());
        return commandLineDTO;
    }

    /**
     * Method to convert a commandLineDTO into a commandLine
     * @param commandLineDTO is a dto of command
     * @return a commandLine
     */
    public static CommandLine commandLineDTOToCommandLine(CommandLineDTO commandLineDTO, ProductService productService, CommandService commandService){
        CommandLine commandLine = new CommandLine();
        commandLine.setId(commandLineDTO.getId());
        commandLine.setUnitePrice(commandLineDTO.getUnitePrice());
        commandLine.setQuantity(commandLineDTO.getQuantity());
        commandLine.setProduct(productService.findOneProductById(commandLineDTO.getProductId()));
        //TODO commandLine.setCommand(commandService.findOneById(commandLineDTO.getCommandId()));
        return commandLine;
    }
}
