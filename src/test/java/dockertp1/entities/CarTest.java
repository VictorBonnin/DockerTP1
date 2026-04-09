package dockertp1.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    public void testCarConstructor() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        assertEquals("ABC123", car.getPlateNumber());
        assertEquals("Toyota", car.getBrand());
        assertEquals(15000.0, car.getPrice());
    }

    @Test
    public void testDefaultConstructor() {
        Car car = new Car();
        assertNull(car.getPlateNumber());
        assertNull(car.getBrand());
        assertEquals(0.0, car.getPrice());
    }

    @Test
    public void testSetPlateNumber() {
        Car car = new Car();
        car.setPlateNumber("XYZ789");
        assertEquals("XYZ789", car.getPlateNumber());
    }

    @Test
    public void testSetBrand() {
        Car car = new Car();
        car.setBrand("BMW");
        assertEquals("BMW", car.getBrand());
    }

    @Test
    public void testSetPrice() {
        Car car = new Car();
        car.setPrice(25000.0);
        assertEquals(25000.0, car.getPrice());
    }

    @Test
    public void testToString() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        String expected = "Car{plateNumber='ABC123', brand='Toyota', price=15000.0}";
        assertEquals(expected, car.toString());
    }
}
