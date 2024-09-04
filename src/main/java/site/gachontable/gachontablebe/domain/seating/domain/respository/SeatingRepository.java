package site.gachontable.gachontablebe.domain.seating.domain.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.user.domain.User;

import java.util.List;

public interface SeatingRepository extends JpaRepository<Seating, Long> {
    List<Seating> findAllByPubOrderBySeatingNum(Pub pub);

    List<Seating> findAllByUser(User user);
}
