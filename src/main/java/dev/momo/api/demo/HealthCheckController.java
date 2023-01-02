package dev.momo.api.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController{
    @GetMapping("/connectionTest")
    public String healthCheck(){
        return "The service is up and running...";
    }
}