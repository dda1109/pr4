package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Card;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {


    List<Card> findAllByUser(User user);
}
