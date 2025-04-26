package de.techfak.gse.template.domain;

import de.techfak.gse.template.web.UsrCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UsrController {
    private UserService userService;

    @Autowired
    public UsrController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public Usr createUser(@RequestBody UsrCmd usrCmd) {
        return userService.createUser(usrCmd.username(), usrCmd.email(), usrCmd.password(),"ROLE_USER");
    }
}
