package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
