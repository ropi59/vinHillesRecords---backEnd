package fr.insy2s.commerce.services;

import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.repositories.RoleRepository;
import fr.insy2s.commerce.services.interfaces.RoleServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements RoleServiceInterface {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }
}
