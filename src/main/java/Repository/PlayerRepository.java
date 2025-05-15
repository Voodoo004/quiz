package Repository;

import model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findBySessionId(Long sessionId);
    List<Player> findBySessionIdOrderByScoreDesc(Long sessionId);
}
