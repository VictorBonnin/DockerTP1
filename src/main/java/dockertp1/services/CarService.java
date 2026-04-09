package dockertp1.services;

import dockertp1.entities.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final List<Car> cars = new ArrayList<>();

    public List<Car> getAllCars() {
        return cars;
    }

    public Optional<Car> getCarByPlateNumber(String plateNumber) {
        return cars.stream()
                .filter(car -> car.getPlateNumber().equals(plateNumber))
                .findFirst();
    }

    public Car addCar(Car car) {
        cars.add(car);
        return car;
    }

    public boolean removeCarByPlateNumber(String plateNumber) {
        return cars.removeIf(car -> car.getPlateNumber().equals(plateNumber));
    }
}
