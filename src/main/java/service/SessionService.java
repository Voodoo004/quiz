package service;


import Repository.GameSessionRepository;
import Repository.PlayerRepository;
import Repository.QuizRepository;
import exception.ResourceNotFoundException;
import model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private final GameSessionRepository sessionRepository;
    private final QuizRepository quizRepository;
    private final PlayerRepository playerRepository;

    public SessionService(GameSessionRepository sessionRepository,
                          QuizRepository quizRepository,
                          PlayerRepository playerRepository) {
        this.sessionRepository = sessionRepository;
        this.quizRepository = quizRepository;
        this.playerRepository = playerRepository;
    }

    private GameSession getSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
    }

    @Transactional
    public GameSession createSession(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        GameSession session = new GameSession();
        session.setQuiz(quiz);
        session.setCode(generateRoomCode());
        session.setStatus(SessionStatus.WAITING);
        session.setCurrentQuestionIndex(0);

        return sessionRepository.save(session);
    }

    @Transactional
    public Player joinSession(Long sessionId, String nickname) {
        GameSession session = getSession(sessionId);

        if (session.getStatus() != SessionStatus.WAITING) {
            throw new IllegalStateException("Cannot join session that has already started");
        }

        Player player = new Player();
        player.setSession(session);
        player.setNickname(nickname);
        player.setScore(0);
        player.setJoinedAt(Instant.now());

        return playerRepository.save(player);
    }

    @Transactional
    public void startSession(Long sessionId) {
        GameSession session = getSession(sessionId);

        if (session.getStatus() != SessionStatus.WAITING) {
            throw new IllegalStateException("Session has already started");
        }

        if (session.getQuiz().getQuestions().isEmpty()) {
            throw new IllegalStateException("Cannot start session with no questions");
        }

        session.setStatus(SessionStatus.IN_PROGRESS);
        session.setStartedAt(Instant.now());
        sessionRepository.save(session);
    }

    @Transactional
    public void nextQuestion(Long sessionId) {
        GameSession session = getSession(sessionId);

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Session is not in progress");
        }

        Quiz quiz = session.getQuiz();
        int nextIndex = session.getCurrentQuestionIndex() + 1;

        if (nextIndex >= quiz.getQuestions().size()) {
            session.setStatus(SessionStatus.COMPLETED);
            sessionRepository.save(session);
        } else {
            session.setCurrentQuestionIndex(nextIndex);
            sessionRepository.save(session);

            Question nextQuestion = quiz.getQuestions().get(nextIndex);
        }
    }

    public List<Player> getLeaderboard(Long sessionId) {
        GameSession session = getSession(sessionId);
        return playerRepository.findBySessionIdOrderByScoreDesc(session.getId());
    }

    public SessionStatus getStatus(Long sessionId) {
        return getSession(sessionId).getStatus();
    }

    public Optional<GameSession> getSessionByCode(String code) {
        return sessionRepository.findByCode(code);
    }

    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}