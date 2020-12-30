package de.infinity.jwt.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class AdminController {

    @GetMapping("/customer")
    public String sayHelloCustomer() {
        return "Hello Customer";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/admin")
    public String sayHelloAdmin() {
        return "Hello admin";
    }
}
