package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.models.CommandLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommandLineRepository extends JpaRepository<CommandLine, Long> {

    Optional<CommandLine> findCommandLineById(Long id);
    void deleteCommandLineById(Long id);
}
