package Repository;

import model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByPlayerIdAndQuestionId(Long playerId, Long questionId);
}
