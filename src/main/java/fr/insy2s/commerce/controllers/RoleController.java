package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost")
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) { this.roleService = roleService; }

    @GetMapping("{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable Long id) {
        ResponseEntity<Role> result;
        try {
            Role role = this.roleService.getRoleById(id);
            if (role != null)
                result = ResponseEntity.ok(role);
            else
                result = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            result = ResponseEntity.badRequest().build();
        }
        return result;
    }
}
