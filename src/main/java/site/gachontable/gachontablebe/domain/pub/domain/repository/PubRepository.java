package site.gachontable.gachontablebe.domain.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

import java.util.Optional;

public interface PubRepository extends JpaRepository<Pub, Integer> {
    Optional<Pub> findByPubId(Integer pubId);
}
