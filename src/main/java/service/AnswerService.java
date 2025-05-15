package service;


import Repository.AnswerRepository;
import Repository.PlayerRepository;
import Repository.QuestionRepository;
import exception.ResourceNotFoundException;
import model.Answer;
import model.Player;
import model.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository,
                         PlayerRepository playerRepository,
                         QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.playerRepository = playerRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Answer submitAnswer(Long playerId, Long questionId, int selectedOption) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Answer answer = new Answer();
        answer.setPlayer(player);
        answer.setQuestion(question);
        answer.setSelectedOption(selectedOption);
        answer.setSubmittedAt(Instant.now());
        answer.setCorrect(selectedOption == question.getCorrectOption());

        if (answer.isCorrect()) {
            player.setScore(player.getScore() + 10);
            playerRepository.save(player);
        }

        return answerRepository.save(answer);
    }
}
