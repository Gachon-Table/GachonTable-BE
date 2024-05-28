package site.gachontable.gachontablebe.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    Optional<Waiting> findByUserAndPub(User user, Pub pub);
}