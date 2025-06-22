package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.web.command.UsrCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller Für den Usr. Fängt REST Abfragen ab und führt die Methoden des UserService aus.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UsrController {
    private final UserService userService;

    @Autowired
    public UsrController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Usr createUser(@RequestBody UsrCmd usrCmd) {
        return userService.createUser(usrCmd.username(), usrCmd.email(), usrCmd.password(), "ROLE_USER");
    }

    @GetMapping("/exists")
    public boolean exists(@RequestParam String email) {
        return userService.existsEmail(email);
    }
}
