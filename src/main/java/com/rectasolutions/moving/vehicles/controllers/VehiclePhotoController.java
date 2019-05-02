package com.rectasolutions.moving.vehicles.controllers;

import com.rectasolutions.moving.vehicles.entities.*;
import com.rectasolutions.moving.vehicles.exceptions.FailToUploadException;
import com.rectasolutions.moving.vehicles.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehiclePhotoController {

    @Autowired
    private VehiclePhotoService vehiclePhotoService;

    // VEHICLE PHOTO

    @GetMapping("/photos")
    public ResponseEntity<List<VehiclePhoto>> getAllVehiclePhotos() {
        return new ResponseEntity<>(vehiclePhotoService.getAllVehiclePhotos(), HttpStatus.OK);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<VehiclePhoto> getVehiclePhotoById(@PathVariable("id") int id) {
        return new ResponseEntity<>(vehiclePhotoService.getVehiclePhotoById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/vehicles/photos/{vehicleId}")
    public ResponseEntity<List<VehiclePhoto>> getVehiclePhotosByVehicleId(@PathVariable("vehicleId") int vehicleId) {
        return new ResponseEntity<>(vehiclePhotoService.getVehiclePhotosByVehicleId(vehicleId), HttpStatus.OK);
    }

    @PostMapping(value = "/photos", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> saveVehiclePhoto(@RequestParam(value = "files") MultipartFile[] files,
                                                   @RequestParam(value = "vehicleId") int vehicleId) {
        try {
            vehiclePhotoService.saveVehiclePhoto(files, vehicleId);
            return new ResponseEntity<>("Photos have been added", HttpStatus.OK);
        } catch (FailToUploadException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/photos/{id}")
    public ResponseEntity<String> deleteVehiclePhoto(@PathVariable int id) {
        Optional<VehiclePhoto> vehiclePhoto = vehiclePhotoService.getVehiclePhotoById(id);
        if (!vehiclePhoto.isPresent()) {
            return new ResponseEntity<>("The photo is not found", HttpStatus.NOT_FOUND);
        }
        return vehiclePhotoService.deleteVehiclePhoto(vehiclePhoto.get());
    }
}