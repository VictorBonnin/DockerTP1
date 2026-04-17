package dockertp1.controllers;

import dockertp1.services.CarService;
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

    @Autowired
    private CarService carService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Nettoyer les voitures entre chaque test
        carService.getAllCars().clear();
    }

    @Test
    public void testGetCarsEmpty() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetCarsWithMultiple() throws Exception {
        // Ajouter deux voitures
        String car1 = """
                {"plateNumber":"MULTI01","brand":"Toyota","price":15000.0}
                """;
        String car2 = """
                {"plateNumber":"MULTI02","brand":"BMW","price":35000.0}
                """;
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(car1))
                .andExpect(status().isOk());
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(car2))
                .andExpect(status().isOk());

        // Vérifier qu'on récupère les deux
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].plateNumber").value("MULTI01"))
                .andExpect(jsonPath("$[1].plateNumber").value("MULTI02"));
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
    public void testGetCarByPlateNumberNotFound() throws Exception {
        // Chercher une voiture qui n'existe pas
        mockMvc.perform(get("/cars/UNKNOWN"))
                .andExpect(status().isOk());
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

    @Test
    public void testDeleteCarNotFound() throws Exception {
        // Supprimer une voiture qui n'existe pas
        mockMvc.perform(delete("/cars/NOTEXIST"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void testAddAndThenDelete() throws Exception {
        // Ajouter une voiture
        String carJson = """
                {"plateNumber":"FLOW01","brand":"Mercedes","price":45000.0}
                """;
        mockMvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(carJson))
                .andExpect(status().isOk());

        // Vérifier qu'elle existe
        mockMvc.perform(get("/cars/FLOW01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Mercedes"));

        // Supprimer
        mockMvc.perform(delete("/cars/FLOW01"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Vérifier que la liste est vide
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
