package com.bitbyte.cargaraage.controllers;

import com.bitbyte.cargaraage.entities.Car;
import com.bitbyte.cargaraage.entities.ImageUploadData;
import com.bitbyte.cargaraage.services.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CarsController {

    private final CarsService carsService;

    @Autowired
    public CarsController(CarsService carsService) {
        this.carsService = carsService;
    }

    @GetMapping("/cars")
    public List<Car> getCars() {
        return new ArrayList<>();
    }

    @PostMapping("/car_garaage/v1/cars")
    public Car saveCar(@RequestBody Car car) {
        return carsService.saveCar(car);
    }

    @PostMapping("/car_garaage/v1/cars/images")
    public ImageUploadData uploadFile(@RequestPart(value = "file") MultipartFile file) {
        try {
            return carsService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}