package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Integer> {

    Optional<Status> findOneById(Long id);
}
