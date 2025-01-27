package site.gachontable.domain.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.domain.pub.domain.Pub;

public interface PubRepository extends JpaRepository<Pub, Integer> {

}
