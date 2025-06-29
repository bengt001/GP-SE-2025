package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.web.command.FriendRequestCmd;
import de.techfak.gse.template.web.command.FriendSendCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final UserService userService;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService, UserService userService) {
        this.friendRequestService = friendRequestService;
        this.userService = userService;
    }

    @PostMapping("/friends/send")
    public FriendRequest sendFriendRequest(@RequestBody FriendSendCmd cmd) {
        Usr requester = userService.loadUserByUsername(cmd.requester());
        if (requester == null || cmd.recipient() == null || cmd.recipient().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültige Eingabe");
        }
        return friendRequestService.sendRequestByRecipientEmail(requester, cmd.recipient());
    }

    @GetMapping("/pending")
    public List<FriendRequest> getPendingRequests(@RequestParam String email) {
        Usr user = userService.loadUserByUsername(email);
        return friendRequestService.getPendingRequests(user);
    }

    @PostMapping("/accept")
    public void acceptRequest(@RequestBody FriendRequestCmd cmd) {
        FriendRequest request = friendRequestService.findById(cmd.requestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anfrage nicht gefunden"));
        friendRequestService.acceptRequest(request);
    }

    @PostMapping("/decline")
    public void declineRequest(@RequestBody FriendRequestCmd cmd) {
        FriendRequest request = friendRequestService.findById(cmd.requestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anfrage nicht gefunden"));
        friendRequestService.declineRequest(request);
    }

    @GetMapping("/list")
    public Set<Usr> getFriends(@RequestParam String email) {
        Usr user = userService.loadUserByUsername(email);
        return friendRequestService.getFriends(user);
    }
}
