package dockertp1.controllers;

import dockertp1.entities.Car;
import dockertp1.services.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class RentServiceRest {

    private final CarService carService;

    public RentServiceRest(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{plateNumber}")
    public Optional<Car> getCarByPlateNumber(@PathVariable String plateNumber) {
        return carService.getCarByPlateNumber(plateNumber);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @DeleteMapping("/{plateNumber}")
    public boolean deleteCar(@PathVariable String plateNumber) {
        return carService.removeCarByPlateNumber(plateNumber);
    }
}
