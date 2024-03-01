package group2.webapp.FinalProject4.services;

import group2.webapp.FinalProject4.models.Card;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CardService {
    List<Card> findAllByUser(User user);

    List<Card> findAll();

    Card getById(Integer id);

    void deleteCardById(Integer id);

    void saveCard(Card card);

    Page<Card> pagingCard(int offset, int pagesize);
}
