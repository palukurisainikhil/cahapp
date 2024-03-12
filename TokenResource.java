package com.example.scan2cash.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Random;

@RestController
public class TokenResource {

    @GetMapping("/generateToken/{mobileNumber}")
    public ResponseEntity<String> generateToken(@PathVariable String mobileNumber){
        // Do a SQL query to retrieve token from the DB which was stored against the mobileNumber
            String token = isActiveTokenPresent(mobileNumber);

            if(Objects.nonNull(token)){
                return ResponseEntity.ok(token);
            }else{
                Random random = new Random();
                int tokenInt = random.nextInt(90000000) + 10000000; // Generate a random number between 100000 and 999999
                String tokenString = String.valueOf(tokenInt);

                // Save the token against the mobileNumber in DB

                return ResponseEntity.ok(tokenString);
        }
    }

    private String isActiveTokenPresent(String mobileNumber) {
        return null;
    }
}
