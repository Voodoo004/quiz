package controller;

import model.Answer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.AnswerService;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<Answer> submitAnswer(
            @RequestParam Long playerId,
            @RequestParam Long questionId,
            @RequestParam int selectedOption) {
        return ResponseEntity.ok(answerService.submitAnswer(
                playerId, questionId, selectedOption));
    }
}