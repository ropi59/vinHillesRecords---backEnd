package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findRefundById(Long id);
}
