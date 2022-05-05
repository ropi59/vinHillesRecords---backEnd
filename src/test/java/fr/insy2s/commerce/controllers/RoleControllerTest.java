package fr.insy2s.commerce.controllers;

import com.google.gson.Gson;
import fr.insy2s.commerce.models.Role;
import fr.insy2s.commerce.services.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.nio.charset.StandardCharsets;

@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoleService roleService;
    private final String route = "/roles";

    //Method findById

    /**
     * Test findRoleById when id is present in database
     */
    @DisplayName("Test findRoleById : id found")
    @Test
    void testFindByIdFound() throws Exception {
        Long id = 1L;
        Role role = new Role(3L, "customer");
        Mockito.when(this.roleService.getRoleById(id)).thenReturn(role);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Gson gson = new Gson();
        Role roleResult = gson.fromJson(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                Role.class);
        Assertions.assertNotNull(roleResult);
        assertThat(roleResult).usingRecursiveComparison().isEqualTo(role);
    }

    /**
     * Test findRoleByID when id is not present in database
     */
    @DisplayName("Test findRoleById : id not found")
    @Test
    void testFindRoleByIdNotFound() throws Exception {
        Long id = 1L;
        Mockito.when(this.roleService.getRoleById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findRoleById when id is not valid
     */
    @DisplayName("Test findRoleById : invalid id")
    @Test
    void testFindRoleByInvalidId() throws Exception {
        Long id = 2992337203685477580L;
        Mockito.when(this.roleService.getRoleById(id)).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test findRoleById when id is null
     */
    @DisplayName("Test findRoleById : null id")
    @Test
    void testFindRoleByNullId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test findRoleById when database is unreachable
     */
    @DisplayName("Test findRoleById : Database unreachable")
    @Test
    void testFindRoleByIdDatabaseUnreachable() throws Exception {
        Long id = 1L;
        Mockito.when(this.roleService.getRoleById(id)).thenThrow(RuntimeException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get(route+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
