package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.UserService;
import de.techfak.gse.template.domain.Usr;
import de.techfak.gse.template.web.command.UsrCmd;
import de.techfak.gse.template.web.dto.UsrDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;




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

    @GetMapping("/profile")
    public ResponseEntity<UsrDto> getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());
        return ResponseEntity.ok(new UsrDto(user));
    }

}
