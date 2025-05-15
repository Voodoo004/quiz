package service;

import Repository.QuestionRepository;
import Repository.QuizRepository;
import model.Question;
import model.Quiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        quiz.setCreatedAt(java.time.Instant.now());
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> getQuiz(Long id) {
        return quizRepository.findById(id);
    }

    @Transactional
    public Question addQuestion(Long quizId, Question question){
        return quizRepository.findById(quizId).map(quiz -> {
            question.setQuiz(quiz);
            return questionRepository.save(question);
        }).orElse(null);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
