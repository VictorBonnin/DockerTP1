package dockertp1.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RentServiceRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetCars() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddCar() throws Exception {
        String carJson = """
                {"plateNumber":"ABC123","brand":"Toyota","price":15000.0}
                """;

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("ABC123"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.price").value(15000.0));
    }

    @Test
    public void testGetCarByPlateNumber() throws Exception {
        // D'abord ajouter une voiture
        String carJson = """
                {"plateNumber":"GET001","brand":"Renault","price":12000.0}
                """;
        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(status().isOk());

        // Puis la récupérer par plaque
        mockMvc.perform(get("/cars/GET001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("GET001"))
                .andExpect(jsonPath("$.brand").value("Renault"));
    }

    @Test
    public void testDeleteCar() throws Exception {
        // D'abord ajouter une voiture
        String carJson = """
                {"plateNumber":"DEL001","brand":"Peugeot","price":18000.0}
                """;
        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(status().isOk());

        // Puis la supprimer
        mockMvc.perform(delete("/cars/DEL001"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
