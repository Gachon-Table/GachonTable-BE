package site.gachontable.gachontablebe.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    Waiting findByUser(User user);
}