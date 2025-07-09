package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.repositories.UserRepository;
import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.web.command.UsrCmd;
import de.techfak.gse.template.web.dto.UsrDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

/**
 * Rest Controller Für den Usr. Fängt REST Abfragen ab und führt die Methoden des UserService aus.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UsrController {
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Instantiates a new Usr controller.
     *
     * @param userService    the user service
     * @param userRepository the user repository
     */
    @Autowired
    public UsrController(UserService userService, UserRepository userRepository) {

        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Create user usr.
     *
     * @param usrCmd the usr cmd
     * @return the usr
     */
    @PostMapping("/register")
    public Usr createUser(@RequestBody UsrCmd usrCmd) {
        return userService.createUser(usrCmd.username(), usrCmd.email(),
                usrCmd.password(), usrCmd.username(), "ROLE_USER");
    }

    /**
     * Exists boolean.
     *
     * @param email the email
     * @return the boolean
     */
    @GetMapping("/exists")
    public boolean exists(@RequestParam String email) {
        return userService.existsEmail(email);
    }

    /**
     * adds a deck with given Id to an User.
     *
     * @param deckId Id of the Deck that is added
     */
    @PostMapping("/usr/decks/{deckId}/add")
    public void addDeck(@PathVariable Long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        userService.addDeck(usr.getUserId(), deckId);
    }

    /**
     * deletes a deck from a user.
     *
     * @param deckId Id of the deck that is deleted
     */
    @DeleteMapping("/usr/decks/{deckId}/delete")
    public void deleteDeck(@PathVariable Long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        userService.deleteDeck(usr.getUserId(), deckId);
    }

    /**
     * gets the list of the names of the decks that the user has activee.
     *
     * @return List of names of the active Decks
     */
    @GetMapping("/usr/activeDecks")
    public List<List<String>> activeDecks() {
        List<List<String>> result = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        result = userService.activeDeckNames(usr.getUserId());
        return result;
    }

    /**
     * Liefert das Profil des aktuell eingeloggten Nutzers.
     *
     * @return ResponseEntity mit den Profildaten des Nutzers als {@link UsrDto}
     */
    @GetMapping("/profile")
    public ResponseEntity<UsrDto> getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());
        return ResponseEntity.ok(new UsrDto(user));
    }

    /**
     * Upload profile picture response entity.
     *
     * @param file the file
     * @return the response entity
     */
    @PostMapping("/profile/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());
        try {
            user.setProfilePictureData(file.getBytes());
            user.setProfilePictureType(file.getContentType());
            userRepository.save(user);
            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload profile picture");
        }
    }

    /**
     * Gets profile picture.
     *
     * @return the profile picture
     */
    @GetMapping("/profile/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());
        byte[] pictureData = user.getProfilePictureData();
        if (pictureData == null) {
            return ResponseEntity.notFound().build();
        }
        String contentType = user.getProfilePictureType();
        return ResponseEntity.ok()
                .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                .body(pictureData);
    }

    /**
     * Gets profile picture of any User.
     *
     * @param username the username
     * @return the profile picture
     */
    @GetMapping("/profile/profile-picture/{username}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        Usr user = userService.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] pictureData = user.getProfilePictureData();
        if (pictureData == null) {
            return ResponseEntity.notFound().build();
        }
        String contentType = user.getProfilePictureType();
        return ResponseEntity.ok()
                .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                .body(pictureData);
    }


}
