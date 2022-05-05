package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.models.Adress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdressRepository extends JpaRepository <Adress, Long> {

    Optional<Adress> findAdressById(Long id);
}
