package fr.insy2s.commerce.services;

import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.repositories.RoleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;

    @BeforeAll
    void init() {roleService = new RoleService(roleRepository);}

    /**
     * Test findRoleById when role exist
     */
    @DisplayName("Test findRoleById : role found")
    @Test
    void testFindRoleByIdRoleFound() {
        Role role = new Role();
        role.setId(3L);
        role.setRoleName("customer");
        BDDMockito.when(this.roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        Role result = this.roleService.getRoleById(role.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(role, result);
    }

    /**
     * Test findRoleById when role is not found
     */
    @DisplayName("Test findRoleById : role not found")
    @Test
    void testFindRoleByIdNotFound() {
        Long id = 123456789L;
        BDDMockito.when(this.roleRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(this.roleService.getRoleById(id));
    }

    /**
     * Test findRoleById when id is invalid
     */
    @DisplayName("Test findRoleById : invalid id")
    @Test
    void testFindRoleByInvalidId(){
        Long id = 2992337203685477580L;
        Assertions.assertNull(this.roleService.getRoleById(id));
    }

    /**
     * Test findRoleById when id is null
     */
    @DisplayName("Test findRoleById : id null")
    @Test
    void testFindRoleByNullId(){
        Long id = null;
        Assertions.assertNull(this.roleService.getRoleById(id));
    }
}
