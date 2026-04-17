package dockertp1.services;

import dockertp1.entities.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService();
    }

    @Test
    public void testAddCar() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        Car result = carService.addCar(car);

        assertEquals("ABC123", result.getPlateNumber());
        assertEquals("Toyota", result.getBrand());
        assertEquals(15000.0, result.getPrice());
    }

    @Test
    public void testAddMultipleCars() {
        carService.addCar(new Car("A1", "Toyota", 15000.0));
        carService.addCar(new Car("A2", "BMW", 35000.0));
        carService.addCar(new Car("A3", "Mercedes", 45000.0));

        assertEquals(3, carService.getAllCars().size());
    }

    @Test
    public void testGetAllCarsEmpty() {
        List<Car> cars = carService.getAllCars();
        assertTrue(cars.isEmpty());
    }

    @Test
    public void testGetAllCarsAfterAdding() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "BMW", 35000.0));

        List<Car> cars = carService.getAllCars();
        assertEquals(2, cars.size());
    }

    @Test
    public void testGetCarByPlateNumberFound() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "BMW", 35000.0));

        Optional<Car> result = carService.getCarByPlateNumber("DEF456");
        assertTrue(result.isPresent());
        assertEquals("BMW", result.get().getBrand());
    }

    @Test
    public void testGetCarByPlateNumberFirst() {
        carService.addCar(new Car("FIRST01", "Audi", 40000.0));
        carService.addCar(new Car("SECOND02", "BMW", 35000.0));

        Optional<Car> result = carService.getCarByPlateNumber("FIRST01");
        assertTrue(result.isPresent());
        assertEquals("Audi", result.get().getBrand());
        assertEquals(40000.0, result.get().getPrice());
    }

    @Test
    public void testGetCarByPlateNumberNotFound() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        Optional<Car> result = carService.getCarByPlateNumber("UNKNOWN");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetCarByPlateNumberEmptyList() {
        Optional<Car> result = carService.getCarByPlateNumber("ANYTHING");
        assertFalse(result.isPresent());
    }

    @Test
    public void testRemoveCarByPlateNumber() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "BMW", 35000.0));

        boolean removed = carService.removeCarByPlateNumber("ABC123");
        assertTrue(removed);
        assertEquals(1, carService.getAllCars().size());
    }

    @Test
    public void testRemoveCarByPlateNumberNotFound() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        boolean removed = carService.removeCarByPlateNumber("UNKNOWN");
        assertFalse(removed);
        assertEquals(1, carService.getAllCars().size());
    }

    @Test
    public void testRemoveFromEmptyList() {
        boolean removed = carService.removeCarByPlateNumber("ANYTHING");
        assertFalse(removed);
    }

    @Test
    public void testRemoveAllCars() {
        carService.addCar(new Car("A1", "Toyota", 15000.0));
        carService.addCar(new Car("A2", "BMW", 35000.0));

        assertTrue(carService.removeCarByPlateNumber("A1"));
        assertTrue(carService.removeCarByPlateNumber("A2"));
        assertTrue(carService.getAllCars().isEmpty());
    }

    @Test
    public void testAddCarReturnsTheSameObject() {
        Car car = new Car("RET01", "Fiat", 8000.0);
        Car returned = carService.addCar(car);
        assertSame(car, returned);
    }
}
