package controller;


import model.GameSession;
import model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<GameSession> createSession(@RequestParam Long quizId) {
        return ResponseEntity.ok(sessionService.createSession(quizId));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Player> joinSession(
            @PathVariable Long id,
            @RequestParam String nickname) {
        return ResponseEntity.ok(sessionService.joinSession(id, nickname));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startSession(@PathVariable Long id) {
        sessionService.startSession(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<List<Player>> getLeaderboard(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getLeaderboard(id));
    }
}